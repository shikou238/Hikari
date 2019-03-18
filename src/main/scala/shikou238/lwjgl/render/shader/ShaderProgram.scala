package shikou238.lwjgl.render.shader

import shikou238.lwjgl.math.matrix._
import shikou238.lwjgl.math.vector._
import shikou238.lwjgl.render.buffer.VertexArrayObject
import shikou238.lwjgl.render.buffer.vertex.{ElementBuffer, VertexBuffer}
import shikou238.lwjgl.render.shader.Attribute.Attribute


class ShaderProgram(val vShader: VertexShader, val fShader: FragmentShader) extends Program{
  attach(vShader)
  attach(fShader)
  bindFragmentDataLocation(fShader.output._1, fShader.output._2)
  link()
  def specifyAttributes(vb: VertexBuffer[_]): Unit ={
    vShader.vao.beHot()
    vb.use()
    beHot()

    val stride = vShader.attributes.map(_._2.size).sum

    var offset = 0
    for((name, at) <- vShader.attributes){
      val posAttrib = getAttributeLocation(name)
      enableVertexAttribute(posAttrib)
      pointVertexAttribute(posAttrib, at.size, stride * java.lang.Float.BYTES, offset * java.lang.Float.BYTES)
      offset += at.size
    }
  }
  def specifyAttributes(vb: VertexBuffer[_], ebo : ElementBuffer): Unit ={
    specifyAttributes(vShader.vao, vShader.attributes, vb, ebo)
  }

  def specifyAttributes(vao: VertexArrayObject, attributes: List[(CharSequence, Attribute)], vb: VertexBuffer[_], ebo : ElementBuffer): Unit ={

    vao.beHot()
    vb.use()
    ebo.bind()
    beHot()

    val stride = attributes.map(_._2.size).sum

    var offset = 0
    for((name, at) <- vShader.attributes){
      val posAttrib = getAttributeLocation(name)
      enableVertexAttribute(posAttrib)
      pointVertexAttribute(posAttrib, at.size, stride * java.lang.Float.BYTES, offset * java.lang.Float.BYTES)
      offset += at.size
    }

  }
  @deprecated
  def specifyAttributes(): Unit ={
    vShader.vao.beHot()

    val stride = vShader.attributes.map(_._2.size).sum

    var offset = 0
    for((name, at) <- vShader.attributes){
      val posAttrib = getAttributeLocation(name)
      enableVertexAttribute(posAttrib)
      pointVertexAttribute(posAttrib, at.size, stride * java.lang.Float.BYTES, offset * java.lang.Float.BYTES)
      offset += at.size
    }
  }
  def apply[T](name: String) : Setter[T] = Setter(name)

  case class Setter[T](name: String){
    def set(value: T): Unit = {
      beHot()
      val location = getUniformLocation(name)
      value match {
        case int: Int =>
          setUniform(location, int)
        case vec3: Vector3f =>
          setUniform(location, vec3)
        case mat4: Matrix4f =>
          setUniform(location, mat4)
      }
    }
    def :=(value: T): Unit = set(value)
  }

}

