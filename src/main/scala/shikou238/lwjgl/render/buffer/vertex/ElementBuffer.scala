package shikou238.lwjgl.render.buffer.vertex

import java.nio.IntBuffer

import org.lwjgl.opengl.GL15
import org.lwjgl.system.MemoryStack
import shikou238.lwjgl.render.buffer.VertexBufferObject
import shikou238.using

class ElementBuffer(element: Int*){

  lazy val EBO = new VertexBufferObject

  using(MemoryStack.stackPush()){(stack) =>{
    val elements: IntBuffer = stack.mallocInt(element.length)
    element.foreach(elements.put _)
    elements.flip

    EBO.bind(GL15.GL_ELEMENT_ARRAY_BUFFER)
    EBO.uploadData(GL15.GL_ELEMENT_ARRAY_BUFFER, elements, GL15.GL_STATIC_DRAW)
  }}

  def bind(): Unit ={
    EBO.bind(GL15.GL_ELEMENT_ARRAY_BUFFER)
  }


}