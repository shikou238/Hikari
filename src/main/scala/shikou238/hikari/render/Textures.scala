package shikou238.hikari.render

import shikou238.lwjgl.render.buffer.Texture

object Textures{
  val top = Texture.load("resources/grass.png")
  val side = Texture.load("resources/grass-side.png")
  val bottom = Texture.load("resources/grass-bottom.png")

  def init(){}
}