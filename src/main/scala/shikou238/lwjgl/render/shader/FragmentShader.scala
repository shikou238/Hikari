package shikou238.lwjgl.render.shader

import org.lwjgl.opengl.GL20

abstract class FragmentShader extends Shader(GL20.GL_FRAGMENT_SHADER){
  type BindFragDataLocation = (Int, CharSequence)
  val output: BindFragDataLocation
  def this(source: CharSequence){
    this()
    setSource(source)
    compile()
  }
}
