package shikou238.lwjgl.core

import shikou238.util.LastWordsApp

import org.lwjgl.glfw
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwPollEvents

abstract class GLFWApp extends LastWordsApp{

  override def fanfare(): Unit = {
    val errorCallback = glfw.GLFWErrorCallback.createPrint

    GLFW.glfwSetErrorCallback(errorCallback)

    if(!GLFW.glfwInit())
      throw new IllegalStateException("Unable to intialize GLFW")
  }

  override def lastWords(): Unit = {
    Context.trend.keys.foreach(println)
    TrashCan.cleanUp()
  }
}
