package shikou238.lwjgl.core

sealed trait Renderable {
  def render()
}
trait Active extends Renderable{
  def update(): Unit
}
trait InActive extends Renderable
