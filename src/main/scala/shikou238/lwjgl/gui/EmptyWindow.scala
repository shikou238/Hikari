package shikou238.lwjgl.gui

class EmptyWindow(width: Int, height: Int, title: CharSequence) extends Window(width, height, title) {
  override def update(): Unit = {}

  override def render(): Unit = {}
}
