package shikou238.lwjgl.render.shader

import org.lwjgl.opengl.GL20
import shikou238.lwjgl.render.buffer.VertexArrayObject

abstract class VertexShader extends Shader(GL20.GL_VERTEX_SHADER) {

  import Attribute.Attribute

  val attributes: List[(CharSequence, Attribute)]

  val vao = new VertexArrayObject

  def this(source: CharSequence) {
    this()
    setSource(source)
    compile()
  }
}
