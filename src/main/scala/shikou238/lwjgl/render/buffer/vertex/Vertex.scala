package shikou238.lwjgl.render.buffer.vertex

import java.nio.FloatBuffer

trait Vertex{
  val size: Int
  def put(buffer: FloatBuffer): Unit
}
