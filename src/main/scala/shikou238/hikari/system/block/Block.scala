package shikou238.hikari.system.block

import shikou238.hikari.render.model.BoardModel
import shikou238.hikari.system.math.Vector3i
import shikou238.lwjgl.math.matrix.Matrix4f

trait Block{
  def draw3d(pos: Vector3i, sides: BlockSide*): Unit
  def drawXZ(pos: Vector3i): Unit
  def drawXY(pos: Vector3i): Unit
  def update(){}
  def solid: Boolean
}
object Block{
  val board = new BoardModel
}

sealed trait BlockSide{
  val rotate: Matrix4f

}
object BlockSide{
  val all = List(Top, North, East, South, West, Bottom)
}
case object Top extends BlockSide{
  override val rotate = Matrix4f(
    Matrix4f.rotate(90f,  1f, 0, 0) *
      Matrix4f.rotate(180f, 0, 1f, 0)
  )
}
case object North extends BlockSide {
  override val rotate = Matrix4f()
}
case object East extends BlockSide{
  override val rotate = Matrix4f.rotate(90f, 0, -1f, 0)
}
case object South extends BlockSide{
  override val rotate = Matrix4f.rotate(180f, 0, -1f, 0)
}
case object West extends BlockSide{
  override val rotate = Matrix4f.rotate(90f, 0, 1f, 0)
}
case object Bottom extends BlockSide{
  override val rotate = Matrix4f.rotate(90f, 1f, 0, 0)
}
