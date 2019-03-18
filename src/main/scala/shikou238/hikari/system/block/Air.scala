package shikou238.hikari.system.block

import shikou238.hikari.system.math.Vector3i

object Air extends Block{
  override def draw3d(pos: Vector3i, sides: BlockSide*): Unit = {}

  override def drawXY(pos: Vector3i): Unit = {}
  override def drawXZ(pos: Vector3i): Unit = {}

  override def solid = false
}
