package shikou238.lwjgl.math.vector

import java.nio.FloatBuffer

class Vectorf(val value: Float*){
  def size = value.size

  def apply(index: Int) = value(index)

  def lengthSquared = value.foldLeft(0f)((r, x) => r + x * x)
  def length = Math.sqrt(lengthSquared).toFloat
  def nomalize = this divide this.length

  def unary_+ = this
  def unary_- = this scale -1f

  def scale(scalar: Float) = new Vectorf(value.map(_ * scalar): _*)
  def divide(scalar: Float) = new Vectorf(value.map(_ / scalar): _*)

  def plus(other: Vectorf) = new Vectorf(value.zip(other.value).map(x => x._1 + x._2): _*)
  def minus(other: Vectorf) = new Vectorf(value.zip(other.value).map(x => x._1 - x._2): _*)

  def dot (other: Vectorf) = value.zip(other.value).foldLeft(0f)((r, x) => r + x._1 * x._2)

  def lerp (other: Vectorf, alpha: Float) = this.scale (1f - alpha) plus (this scale alpha)

  def toBuffer(buffer: FloatBuffer) = {
    value.foldLeft(buffer)((b, f) => b.put(f))
    //    buffer.flip()
  }
  override def toString = {
    val l = value.map(_.toString)

    val max = l.map(_.size).foldLeft(0)((max, x) => {if(max < x ) x else max})
    l.map(s =>s"|%${max}s|".format(s)).mkString(System.lineSeparator)
  }
}
case class Vector2f(x: Float = 0f,
                    y: Float = 0f)extends Vectorf (x, y)
object Vector2f{
  def apply(v: Vectorf): Vector2f = Vector2f(v.value.head, v.value.tail.head)
}
case class Vector3f(x: Float = 0f,
                    y: Float = 0f,
                    z: Float = 0f)extends Vectorf (x, y, z){
  def * (s: Float): Vector3f = Vector3f(this scale s)
  def + (other: Vector3f): Vector3f = Vector3f(this plus other)
  def - (other: Vector3f): Vector3f = Vector3f(this minus  other)
  def toVector4f: Vector4f = Vector4f(x,y,z,1.0f)
}
object Vector3f{
  def apply(v: Vectorf): Vector3f =
    Vector3f(
      v.value.head,
      v.value.tail.head,
      v.value.tail.tail.head)
}
case class Vector4f(x: Float = 0f,
                    y: Float = 0f,
                    z: Float = 0f,
                    w: Float = 0f)extends Vectorf (x, y, z, w)
object Vector4f{
  def apply(v: Vectorf): Vector4f = Vector4f(
    v.value.head,
    v.value.tail.head,
    v.value.tail.tail.head,
    v.value.tail.tail.tail.head)
}
//TODO rewrite at all