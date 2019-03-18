package shikou238.hikari.system.block

import shikou238.hikari.system.math.Vector3i
import shikou238.lwjgl.render.buffer.Texture

import shikou238.hikari.render.Textures._

object Grass extends Block {
  import Block.board

  override def solid: Boolean = true

  override def draw3d(pos: Vector3i,sides: BlockSide*): Unit = {

    board.position = (pos.x,pos.y,pos.z)

    for(sid <- sides){
      board.side = sid
      val tex: Texture = sid match {
        case Top => top
        case Bottom => bottom
        case _ => side
      }
      tex.beHot()
      board.render()
    }
  }
  override def drawXY(pos: Vector3i): Unit = {
    draw3d(pos, North)
  }
  override def drawXZ(pos: Vector3i): Unit = {
    draw3d(pos, BlockSide.all: _*) //TODO all to Top
  }
}
