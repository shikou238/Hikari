package shikou238.lwjgl.render.shader

import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL11.GL_TRUE
import shikou238.lwjgl.core.TrashCan.FateOfTrash

class Shader(`type` : Int) extends FateOfTrash{
  val id = glCreateShader(`type`)

  def this(`type`: Int, source: CharSequence){
    this(`type`)
    setSource(source)
    compile()
  }

  def setSource (source: CharSequence) {glShaderSource(id, source)}

  def compile(): Unit = {
    glCompileShader(id)
    checkStatus()
  }

  private[this] def checkStatus(): Unit = {
    val status = glGetShaderi(id, GL_COMPILE_STATUS)
    if (status != GL_TRUE) throw new RuntimeException(glGetShaderInfoLog(id))
  }

  override def trash() = glDeleteShader(id)
}
object Shader{
  def load(`type`: Int, path: String): Shader = {

    import scala.io.Source

    val s = Source.fromFile(path)
    var source: String = ""
    try {
      source = s.getLines().mkString(System.lineSeparator())
    } finally {
      s.close
    }
    new Shader(`type`, source)
  }
}
