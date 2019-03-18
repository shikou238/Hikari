package shikou238.lwjgl.render.buffer

import org.lwjgl.opengl.GL30._
import shikou238.lwjgl.core.Context.Fashonable
import shikou238.lwjgl.core.TrashCan.FateOfTrash

class VertexArrayObject extends FateOfTrash with Fashonable{
  val id = glGenVertexArrays()

  override val indentifier: String = "VAO"

  def hot(): Unit ={
    glBindVertexArray(id)
  }
  override def trash(): Unit ={
    glDeleteVertexArrays(id)
  }
}