package shikou238.lwjgl.render.buffer.vertex

import java.nio.FloatBuffer

import shikou238.lwjgl.render.buffer.VertexBufferObject

trait VertexBuffer[T <: Vertex]{
  def put(buffer: FloatBuffer): Unit
  val VBO: VertexBufferObject
  def use(): Unit
}

