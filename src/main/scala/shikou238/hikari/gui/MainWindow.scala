package shikou238.hikari.gui

import org.lwjgl.glfw.GLFW.{GLFW_KEY_TAB, GLFW_PRESS, GLFW_RELEASE, glfwPollEvents}
import org.lwjgl.opengl.GL11._
import shikou238.hikari.system.World
import shikou238.hikari.system.block.Grass
import shikou238.hikari.system.math.Vector3i
import shikou238.lwjgl.gui.Window
import shikou238.lwjgl.math.vector._
import shikou238.hikari.system.block.Block
import shikou238.lwjgl.render.Camera



class MainWindow(width: Int, height: Int)
  extends Window(width, height, "Hikari MainWindow") {
  var renderMode: RenderMode = PersepectiveMode

  glEnable(GL_DEPTH_TEST)
  glDepthFunc(GL_LEQUAL)
  glDisable(GL_CULL_FACE)
  glCullFace(GL_BACK)


  PersepectiveMode.camera = new Camera(aspect = ratio, pos = Vector3f(5,2,7))
  XZOrthoMode.camera = new Camera(aspect = ratio, pos = Vector3f(5,2,5), look = Vector3f(0, -1f, 0), up = Vector3f(0f, 0f, -1f))
  XYOrthoMode.camera = new Camera(aspect = ratio, pos = Vector3f(5,5,2), look = Vector3f(0, 0, -1f))

  val step = 0.1f

  val pressingKeys = collection.mutable.Map.empty[Int ,Boolean]

  val pressedKeys = collection.mutable.Map.empty[Int ,Boolean]

  keyCallback add {
    case (_, key, _, GLFW_PRESS, _) =>
      if(pressingKeys.keys.exists(_ == key)){
        pressingKeys(key) = true
        pressedKeys(key) = true
      } else {
        pressingKeys += (key -> true)
        pressedKeys += (key -> true)
      }
    case (_, key, _, GLFW_RELEASE, _) =>
      if(pressingKeys.keys.exists(_ == key)) {
        pressingKeys(key) = false
        pressedKeys(key) = false
      }else {
        pressingKeys += (key -> false)
        pressedKeys += (key -> false)
      }
    case _ =>
  }

  val range = 10

  val world = new World(Vector3i(range, range, range))

  for(x <- Range(0, range); z <- Range(0,range))
    world.map.set(Grass,x, 0, z)

  override def update(): Unit = {

    renderMode.update(pressingKeys, pressedKeys)

    pressedKeys.retain((_, b) =>b).keys.foreach {
      case GLFW_KEY_TAB =>
        renderMode = renderMode.next
      case _ =>
    }
    pressedKeys.clear()

    Block.board.update()
  }



  override def render(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    renderMode.render(world)



    swapBuffers()

    glfwPollEvents()
  }
}


