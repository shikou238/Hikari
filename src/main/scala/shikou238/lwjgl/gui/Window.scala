package shikou238.lwjgl.gui

import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.{GLFW, GLFWKeyCallback}
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack
import shikou238.lwjgl.core.{Active, TrashCan}
import TrashCan.FateOfTrash
import shikou238.lwjgl.core.Context.Fashonable
import shikou238._


abstract class Window
  (width: Int,
   height: Int,
   title: CharSequence,
   var _vsync: Boolean = true,
   monitor: MonitorID = 0L,
   share: WindowID = 0L) extends Active with FateOfTrash with Fashonable{


  glfwDefaultWindowHints()

  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
  glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
  glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

  val id : WindowID = glfwCreateWindow(width, height, title, monitor, share)

  if(id == 0){
    glfwTerminate()
    throw new RuntimeException("Failed to create the GLFW window.")
  }

  private[this] val _size = glfwGetVideoMode(glfwGetPrimaryMonitor())

  glfwSetWindowPos(id, (_size.width- width)/2, (_size.height - height)/2)

  glfwMakeContextCurrent(id)
  GL.createCapabilities()

  if(_vsync){
    glfwSwapInterval(1)
  }

  val keyCallback = new KeyCallBack(id)

  keyCallback add {
    case (window: WindowID, GLFW_KEY_ESCAPE, _, GLFW_PRESS, _) =>
      glfwSetWindowShouldClose(window, true)
    case _ =>
  }

  def closing = glfwWindowShouldClose(id)

  def isVSyncEnabled = _vsync

  def vsync_=(s: Boolean): Unit ={
    this._vsync = s
    if(s){
      glfwSwapInterval(1)
    }else{
      glfwSwapInterval(0)
    }
  }

  def ratio: Float = {
    var r = .0f
    using(MemoryStack.stackPush) {(stack) => {
      val window = this.id
      val width = stack.mallocInt(1)
      val height = stack.mallocInt(1)
      GLFW.glfwGetFramebufferSize(window, width, height)
      r = width.get / height.get.toFloat
    }}
    r
  }

  def swapBuffers(): Unit ={
    glfwSwapBuffers(this.id)
  }

  def hide(): Unit ={
    glfwHideWindow(id)
  }

  def show(): Unit ={
    glfwShowWindow(id)
  }

  override def trash(): Unit ={
    debug(title)
    glfwDestroyWindow(id)
  }

  override val indentifier = "Window"

  override protected def hot(): Unit ={
    GLFW.glfwMakeContextCurrent(this.id)
  }
}

class KeyCallBack(id: WindowID) extends FateOfTrash {
  type Reaction = (/*window: */Long,/* key: */Int,/* scancode: */Int, /*action: */Int,/* mods: */Int) => Unit

  private[this] var reactions : List[Reaction] = List.empty

  val toGlfw = new GLFWKeyCallback() {
    override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit =
      if(!glfwWindowShouldClose(id))
        reactions.map(_(window, key, scancode, action, mods))
  }

  glfwSetKeyCallback(id, this.toGlfw)

  def add (reaction: Reaction): Unit ={
    reactions ::= reaction
    // glfwSetKeyCallback(id, this.toGlfw)
  }

  override def trash(): Unit = toGlfw.free()
}

