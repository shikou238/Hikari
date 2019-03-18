package shikou238.lwjgl.render.buffer

import java.nio.ByteBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.system.MemoryStack
import shikou238.using
import org.lwjgl.stb.STBImage.{stbi_failure_reason, stbi_load, stbi_set_flip_vertically_on_load}
import shikou238.lwjgl.core.Context.Fashonable
import shikou238.lwjgl.core.TrashCan.FateOfTrash

object Texture {

  def create(width: Int, height: Int, data: ByteBuffer): Texture = {
    val texture = new Texture
    texture.width_=(width)
    texture.height_=(height)
    texture.beHot()
    texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER)
    texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER)
    texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data)
    texture
  }


  def load(path: String): Texture = {
    var image: ByteBuffer = null //気持ち悪いが仕方なし
    var width = 0
    var height = 0
    using(MemoryStack.stackPush()){(stack) =>{
      val w, h = stack.mallocInt(1)
      val comp = stack.mallocInt(1)

      stbi_set_flip_vertically_on_load(true)
      image = stbi_load(path, w, h, comp, 4)

      if (image == null)
        throw new RuntimeException("Failed to load a texture file!" + System.lineSeparator + stbi_failure_reason)

      width = w.get
      height = h.get
    }}
    create(width, height, image)
  }
}

class Texture() extends FateOfTrash with Fashonable{
  val id = glGenTextures()

  private var _width = 0
  private var _height = 0



  def setParameter(name: Int, value: Int): Unit = {
    glTexParameteri(GL_TEXTURE_2D, name, value)
  }

  def uploadData(width: Int, height: Int, data: ByteBuffer): Unit = {
    uploadData(GL_RGBA8, width, height, GL_RGBA, data)
  }

  def uploadData(internalFormat: Int, width: Int, height: Int, format: Int, data: ByteBuffer): Unit = {
    glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data)
  }

  def width: Int = _width

  def width_=(width: Int): Unit = {
    if (width > 0) this._width = width
  }

  def height: Int = _height
  def height_=(height: Int): Unit = {
    if (height > 0) this._height = height
  }
  def active(index: Int): Unit ={
    import org.lwjgl.opengl.GL13
    require(0 < index && index <= 31)
    GL13.glActiveTexture(GL13.GL_TEXTURE0 + index)
    beHot()
  }
  override val indentifier: String = "Texture"
  override def hot(): Unit = glBindTexture(GL_TEXTURE_2D, id)

  override def trash(): Unit =  glDeleteTextures(id)
}//TODO rewrite
