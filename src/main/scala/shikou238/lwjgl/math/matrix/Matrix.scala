package shikou238.lwjgl.math.matrix

import java.nio.FloatBuffer

import shikou238.lwjgl.math.vector._

class Matrixf(val value: Vectorf*){
  require(!value.exists(_.size != value.head.size))
  def * (vec: Vectorf): Vectorf = {
    require(vec.value.size == value.size)
    var temp = value
    new Vectorf(vec.value.map{ _ =>
    {
      val result: Float = (vec.value zip temp.map(_.value.head) ).foldLeft (0f)((r: Float, x:(Float, Float)) => (r + x._1 * x._2) )
      temp = temp.map(x => new Vectorf(x.value.tail: _*))
      result
    }}: _*)
  }
  def * (mat: Matrixf): Matrixf ={
    new Matrixf(mat.value.map(this.*): _*)
  }
  def * (scalar: Float): Matrixf = new Matrixf(value.map(_.scale(scalar)): _*)
  def toVector = {require(value.size == 1) ; value.head}
  def toBuffer(buffer: FloatBuffer): Unit ={
    value.foreach(_.value.foreach(buffer.put))
    //    buffer.flip()
  }
  override def toString: String ={
    def extract(s: String) = s.filter(_ != '|')

    var l = value.map(_.toString.split(System.lineSeparator).map(extract))

    Range(0, l.head.size).map{ _ =>{
      val r = "|" + l.map(_.head).mkString(" ") + "|"
      l = l.map(_.tail)
      r
    }}.mkString(System.lineSeparator)
  }
}
object Matrixf{
  def identity(n: Int): Matrixf = {
    new Matrixf((1 to n).map{ i =>
      new Vectorf((1 to n).map(x => if(x == i) 1f else 0f): _*)
    }:_*)
  }
}
case class Matrix2f(v0: Vector2f,v1: Vector2f) extends Matrixf(v0, v1)
object Matrix2f{
  def apply(m00: Float, m10: Float,
            m01: Float, m11: Float): Matrix2f =
    Matrix2f(Vector2f(m00, m01),
      Vector2f(m10, m11))

  def apply(m: Matrixf): Matrix2f =
    new Matrix2f(
      Vector2f(m.value.head),
      Vector2f(m.value.tail.head))
}
case class Matrix3f(v0: Vector3f, v1: Vector3f, v2: Vector3f) extends Matrixf(v0, v1, v2)
object  Matrix3f{
  def apply(m00: Float, m10: Float, m20: Float,
            m01: Float, m11: Float, m21: Float,
            m02: Float, m12: Float, m22: Float): Matrix3f =
    Matrix3f(
      Vector3f(m00, m01, m02),
      Vector3f(m10, m11, m12),
      Vector3f(m20, m21, m22))

  def apply(m: Matrixf): Matrix3f =
    new Matrix3f(
      Vector3f(m.value.head),
      Vector3f(m.value.tail.head),
      Vector3f(m.value.tail.tail.head))
}
case class Matrix4f(v0: Vector4f, v1: Vector4f, v2: Vector4f, v3: Vector4f) extends Matrixf(v0, v1, v2, v3)
object Matrix4f{
  def apply(m00: Float = 1f, m01: Float = 0f, m02: Float = 0f,m03: Float = 0f,
            m10: Float = 0f, m11: Float = 1f, m12: Float = 0f,m13: Float = 0f,
            m20: Float = 0f, m21: Float = 0f, m22: Float = 1f,m23: Float = 0f,
            m30: Float = 0f, m31: Float = 0f, m32: Float = 0f,m33: Float = 1f) : Matrix4f=
    new Matrix4f(Vector4f(m00, m10, m20, m30),
      Vector4f(m01, m11, m21, m31),
      Vector4f(m02, m12, m22, m32),
      Vector4f(m03, m13, m23, m33))

  def apply(m: Matrixf): Matrix4f =
    new Matrix4f(
      Vector4f(m.value.head),
      Vector4f(m.value.tail.head),
      Vector4f(m.value.tail.tail.head),
      Vector4f(m.value.tail.tail.tail.head))

  def orthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f ={
    val tx = -(right + left) / (right - left)
    val ty = -(top + bottom) / (top - bottom)
    val tz = -(far + near) / (far - near)
    Matrix4f(
      m00 = 2f / (right - left),
      m11 = 2f / (top - bottom),
      m22 = -2f / (far - near),
      m03 = tx,
      m13 = ty,
      m23 = tz
    )
  }

  def identity = Matrix4f(Matrixf.identity(4))
  def rotate(angle: Float, x: Float, y: Float, z: Float): Matrix4f = {
    import StrictMath.{sin, cos}
    import Math.toRadians

    val c = cos(toRadians(angle)).toFloat
    val s = sin(toRadians(angle)).toFloat
    val v = Vector3f(x, y, z).nomalize

    val Seq(q ,w ,e): Seq[Float] = v.value
    Matrix4f(
      q*q*(1f-c)+c  , w*q*(1f-c)-e*s, q*e*(1f-c)+w*s, 0f,
      q*w*(1f-c)+e*s, w*w*(1f-c)+c  , w*e*(1f-c)-q*s, 0f,
      q*e*(1f-c)-w*s, w*e*(1f-c)+q*s, e*e*(1f-c)+c,   0f,
      0f            , 0f            , 0f          ,   1f
    )
  }
  def rotate(cos: Float, sin: Float,x: Float, y: Float, z: Float): Matrix4f = {
    val c = cos
    val s = sin
    val v = Vector3f(x, y, z).nomalize

    val Seq(q ,w ,e): Seq[Float] = v.value
    Matrix4f(
      q*q*(1f-c)+c  , w*q*(1f-c)-e*s, q*e*(1f-c)+w*s, 0f,
      q*w*(1f-c)+e*s, w*w*(1f-c)+c  , w*e*(1f-c)-q*s, 0f,
      q*e*(1f-c)-w*s, w*e*(1f-c)+q*s, e*e*(1f-c)+c,   0f,
      0f            , 0f            , 0f          ,   1f
    )
  }

  def perspective(fovy: Float, aspect: Float, near:Float, far: Float): Matrix4f = {

    val f = (1f / Math.tan(Math.toRadians(fovy) / 2f)).toFloat

    Matrix4f(
      m00 = f / aspect,
      m11 = f,
      m22 = (far + near) / (near - far),
      m32 = -1f,
      m23 = (2f * far * near) / (near - far),
      m33 = 0f
    )
  }
  def translate(x: Float, y: Float, z: Float)= Matrix4f (
    m03 = x,
    m13 = y,
    m23 = z,
  )
  def rotate(angle: Float,x: Float, y: Float, z: Float,px: Float, py: Float, pz: Float): Matrix4f =
    Matrix4f(translate(px, py, pz) * rotate(angle, x, y, z) * translate(-px, -py, -pz))
}
//TODO rewrite at all