package shikou238.hikari.main

import org.lwjgl.glfw.GLFW.glfwPollEvents
import shikou238.lwjgl.core.GLFWApp
import shikou238.lwjgl.gui.Window
import shikou238.hikari.gui.MainWindow
import shikou238.hikari.render.Textures

object Hikari extends GLFWApp{
  var windows = List.empty[Window]

  windows ::= new MainWindow(640, 480)

  Textures.init()

  while(!windows.isEmpty){
    windows.foreach{w =>{
      w.beHot()
      w.update()
      w.render()
    }}
    glfwPollEvents()
    windows = windows.filter(w => if (w.closing) {w.hide();false } else true)
  }
}
