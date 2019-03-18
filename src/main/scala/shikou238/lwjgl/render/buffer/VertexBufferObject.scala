package shikou238.lwjgl.render.buffer

import java.nio.{FloatBuffer, IntBuffer}

import org.lwjgl.opengl.GL15._
import shikou238.lwjgl.core.Context.Fashonable
import shikou238.lwjgl.core.TrashCan.FateOfTrash

class VertexBufferObject extends FateOfTrash with Fashonable{

  val id = glGenBuffers()

  override val indentifier: String = "VBO"

  override protected def hot(): Unit = {}//dammy impliments

  def bind(target: Int): Unit ={
    glBindBuffer(target, id)
    beHot()
  }

  def uploadData(target: Int, data: FloatBuffer, usage: Int): Unit = {
    glBufferData(target, data, usage)
  }
  def uploadData(target: Int, size: Long, usage: Int): Unit = {
    glBufferData(target, size, usage)
  }
  def uploadData(target: Int, data: IntBuffer, usage: Int): Unit = {
    glBufferData(target, data, usage)
  }

  def uploadSubData(target: Int, offset: Long, data: FloatBuffer): Unit = {
    glBufferSubData(target, offset, data)
  }

  def trash(): Unit = {
    glDeleteBuffers(id)
  }
}
