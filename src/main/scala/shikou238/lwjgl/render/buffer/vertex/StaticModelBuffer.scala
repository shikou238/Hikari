package shikou238.lwjgl.render.buffer.vertex

import java.nio.FloatBuffer

import org.lwjgl.opengl.GL15
import org.lwjgl.system.MemoryStack
import shikou238.lwjgl.render.buffer.VertexBufferObject
import shikou238.using

class StaticModelBuffer[T<: Vertex](vertexes: T*) extends VertexBuffer[T]{
  require(!vertexes.isEmpty)

  override def put(buffer: FloatBuffer): Unit = {
    vertexes.foreach(_.put(buffer))
  }
  override lazy val VBO = {
    val vbo = new VertexBufferObject

    using(MemoryStack.stackPush) {(stack) =>{
      /* Vertex data */
      val vertices = stack.mallocFloat(vertexes.length * vertexes(0).size)
      put(vertices)
      vertices.flip
      /* Generate Vertex Buffer Object */
      vbo.bind(GL15.GL_ARRAY_BUFFER)
      vbo.uploadData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
    }}

    vbo
  }
  def use(): Unit ={
    VBO.bind(GL15.GL_ARRAY_BUFFER)
  }
}