package shikou238.lwjgl.render.shader

import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30.glBindFragDataLocation
import org.lwjgl.system.MemoryStack
import shikou238.lwjgl.core.Context.Fashonable
import shikou238.lwjgl.core.TrashCan.FateOfTrash
import shikou238.lwjgl.math.matrix.{Matrix2f, Matrix3f, Matrix4f}
import shikou238.lwjgl.math.vector.{Vector2f, Vector3f, Vector4f}
import shikou238.using


class Program extends FateOfTrash with Fashonable{
  val id = glCreateProgram()

  def attach(shader: Shader){glAttachShader(id, shader.id)}

  def bindFragmentDataLocation(number: Int, name: CharSequence): Unit = {
    glBindFragDataLocation(id, number, name)
  }
  def link(): Unit = {
    glLinkProgram(id)
    checkStatus()
  }
  def checkStatus(): Unit = {
    val status = glGetProgrami(id, GL_LINK_STATUS)
    if (status != GL_TRUE) throw new RuntimeException(glGetProgramInfoLog(id))
  }

  type AttributeLocation = Int

  def getAttributeLocation(name: CharSequence): AttributeLocation = glGetAttribLocation(id, name)

  def enableVertexAttribute(location: AttributeLocation): Unit = {
    glEnableVertexAttribArray(location)
  }
  def disableVertexAttribute(location: AttributeLocation): Unit = {
    glDisableVertexAttribArray(location)
  }
  def pointVertexAttribute(location: AttributeLocation, size: Int, stride: Int, offset: Int): Unit = {
    glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset)
  }
  def getUniformLocation(name: CharSequence): AttributeLocation = glGetUniformLocation(id, name)



  def setUniform(location: AttributeLocation, value: Int): Unit = {
    glUniform1i(location, value)
  }
  def setUniform(location: AttributeLocation, value: Vector2f): Unit = {
    using(MemoryStack.stackPush){(stack) =>{
      val buffer = stack.mallocFloat(2)
      value.toBuffer(buffer)
      buffer.flip()
      glUniform2fv(location, buffer)
    }}
  }
  def setUniform(location: AttributeLocation, value: Vector3f): Unit = {
    using(MemoryStack.stackPush){(stack) =>{
      val buffer = stack.mallocFloat(3)
      value.toBuffer(buffer)
      buffer.flip()
      glUniform3fv(location, buffer)
    }}
  }
  def setUniform(location: AttributeLocation, value: Vector4f): Unit = {
    using(MemoryStack.stackPush){(stack) =>{
      val buffer = stack.mallocFloat(4)
      value.toBuffer(buffer)
      buffer.flip()
      glUniform4fv(location, buffer)
    }}
  }


  def setUniform(location: AttributeLocation, value: Matrix2f): Unit = {
    using(MemoryStack.stackPush){(stack) =>{
      val buffer = stack.mallocFloat(2*2)
      value.toBuffer(buffer)
      buffer.flip()
      glUniformMatrix2fv(location, false, buffer)
    }}
  }
  def setUniform(location: AttributeLocation, value: Matrix3f): Unit = {
    using(MemoryStack.stackPush){(stack) =>{
      val buffer = stack.mallocFloat(3*3)
      value.toBuffer(buffer)
      buffer.flip()
      glUniformMatrix3fv(location, false, buffer)
    }}
  }
  def setUniform(location: AttributeLocation, value: Matrix4f): Unit = {
    using(MemoryStack.stackPush){(stack) =>{
      val buffer = stack.mallocFloat(4*4)
      value.toBuffer(buffer)
      buffer.flip()
      glUniformMatrix4fv(location, false, buffer)
    }}
  }
  override val indentifier = "shader"

  override def hot(): Unit = {
    glUseProgram(id)
  }

  def trash(): Unit = {
    glDeleteProgram(id)
  }
}
