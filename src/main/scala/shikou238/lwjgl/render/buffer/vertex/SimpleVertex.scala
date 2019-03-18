package shikou238.lwjgl.render.buffer.vertex

import java.nio.FloatBuffer

import shikou238.lwjgl.math.vector.Vector3f

case class SimpleVertex(position: Vector3f,color: Vector3f) extends Vertex {

  override val size = 6

  def put(buffer: FloatBuffer): Unit ={
    position.toBuffer(buffer)
    color.toBuffer(buffer)
  }
}