package shikou238.lwjgl.render.shader

object Attribute extends Enumeration {
  val float = Attribute(1)
  val vec2 = Attribute(2)
  val vec3 = Attribute(3)
  val vec4 = Attribute(4)
  case class Attribute(size: Int) extends Val
}
