package shikou238.lwjgl.render

import shikou238.lwjgl.math.matrix._
import shikou238.lwjgl.math.vector._

class Camera(
              var pos: Vector3f = Vector3f(0, 0, 0),
              var look: Vector3f = Vector3f(0, 0, -1),
              var up: Vector3f = Vector3f(0, 1, 0),
              var fov: Float = 90f,
              var aspect: Float = 1f,
              var distance: Float = 10f,
              var range: Float = 5f
            ){
  def right: Vector3f = Vector3f(look.y*up.z-look.z*up.y, look.z*up.x-look.x*up.z, look.x*up.y-look.y*up.x)

  def view: Matrix4f = {
    val Seq(lx, ly, lz) = look.value
    val Seq(ux, uy, uz) = up.value
    val Seq(rx, ry, rz) = right.value
    val Seq(tx, ty, tz) = pos.value
    Matrix4f(
      rx , ry, rz, -tx*rx + -ty*ry + -tz*rz,
      ux , uy, uz, -tx*ux + -ty*uy + -tz*uz,
      -lx,-ly,-lz,  tx*lx +  ty*ly +  tz*lz,
      0,   0,  0,   1
    )
  }
  val nearZ = 0.001953125f

  def perspective: Matrix4f = Matrix4f.perspective(fov, aspect, nearZ, distance)
  def ortho: Matrix4f = Matrix4f.orthographic(-range*aspect, range*aspect, -range, range, nearZ, distance)
}
