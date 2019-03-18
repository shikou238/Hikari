package shikou238.hikari.gui

import org.lwjgl.glfw.GLFW._
import shikou238.hikari.system.World
import shikou238.hikari.system.block.Block
import shikou238.hikari.system.math.Vector3i

import scala.collection.mutable.Map
import shikou238.lwjgl.math.vector._
import shikou238.lwjgl.math.matrix._
import shikou238.lwjgl.render.Camera

sealed trait RenderMode{
  def next: RenderMode
  def update(pressingKeys: Map[Int, Boolean], pressedKeys: Map[Int, Boolean]): Unit
  var camera: Camera = _
  def render(world: World): Unit
}

object PersepectiveMode extends RenderMode {
  def next = XZOrthoMode
  def update(pressingKeys: Map[Int, Boolean], pressedKeys: Map[Int, Boolean]) = {
    val step = 0.1f

    pressingKeys.retain((_, b) =>b).keys.foreach {
      case GLFW_KEY_A =>
        camera.pos += camera.right * -step
      case GLFW_KEY_D =>
        camera.pos += camera.right * step
      case GLFW_KEY_W =>
        camera.pos += camera.look * step
      case GLFW_KEY_S =>
        camera.pos += camera.look * -step
      case GLFW_KEY_LEFT_SHIFT =>
        camera.pos += camera.up * -step
      case GLFW_KEY_LEFT_CONTROL =>
        camera.pos += camera.up * step
      case GLFW_KEY_Q =>
        camera.look =
          Vector3f(
            Matrix4f.rotate(-step*8, 0, 1, 0) *
              (camera.look.toVector4f)
          )
      case GLFW_KEY_E =>
        camera.look =
          Vector3f(
            Matrix4f.rotate(step*8, 0, 1, 0) *
              (camera.look.toVector4f)
          )
      case _ =>
    }
    Block.board.program("view") := camera.view

    Block.board.program("projection") := camera.perspective

    Block.board.program("camera") := camera.pos
  }
  def render(world: World): Unit = {
    val Vector3i(x,y,z) = world.size
    world.map.draw(Vector3i(0,0,0), Vector3i(x-1,y-1,z-1), this)
  }
}
object XZOrthoMode extends RenderMode {
  def next = XYOrthoMode

  var renderY = 0

  def update (pressingKeys: Map[Int, Boolean], pressedKeys: Map[Int, Boolean]): Unit ={
    val step = 0.1f

    pressingKeys.retain((_, b) =>b).keys.foreach {
      case GLFW_KEY_A =>
        camera.pos += camera.right * -step
      case GLFW_KEY_D =>
        camera.pos += camera.right * step
      case GLFW_KEY_W =>
        camera.pos += camera.up * step
      case GLFW_KEY_S =>
        camera.pos += camera.up * -step
      case _ =>
    }
    pressingKeys.retain((_, b) =>b).keys.foreach {
      case GLFW_KEY_LEFT_CONTROL =>
        renderY -= 1
      case GLFW_KEY_LEFT_SHIFT =>
        renderY += 1
      case _ =>
    }
    Block.board.program("view") := camera.view

    Block.board.program("projection") := camera.ortho

    Block.board.program("camera") := camera.pos
  }
  def render(world: World) {
    import scala.math.{min, max}

    val Vector3i(x,y,z) = world.size

    renderY = max(0, min(renderY, y-1))


    world.map.draw(Vector3i(0,renderY,0), Vector3i(x-1,renderY,z-1), this)
  }
}
object XYOrthoMode extends RenderMode {
  def next = PersepectiveMode
  def update(pressingKeys: Map[Int, Boolean], pressedKeys: Map[Int, Boolean]): Unit ={
    val step = 0.1f

    pressingKeys.retain((_, b) =>b).keys.foreach {
      case GLFW_KEY_A =>
        camera.pos += camera.right * -step
      case GLFW_KEY_D =>
        camera.pos += camera.right * step
      case GLFW_KEY_W =>
        camera.pos += camera.up * step
      case GLFW_KEY_S =>
        camera.pos += camera.up * -step
      case GLFW_KEY_Q =>
        camera.look =
          Vector3f(
            Matrix4f.rotate(-step*8, 1, 0, 0) *
              (camera.look.toVector4f)
          )
      case GLFW_KEY_E =>
        camera.look =
          Vector3f(
            Matrix4f.rotate(step*8, 1, 0, 0) *
              (camera.look.toVector4f)
          )
      case _ =>
    }

    Block.board.program("view") := camera.view

    Block.board.program("projection") := camera.ortho

    Block.board.program("camera") := camera.pos
  }
  def render(world: World){
    val Vector3i(x,y,z) = world.size
    world.map.draw(Vector3i(0,0,0), Vector3i(x-1,y-1,z-1), this)
  }
}
