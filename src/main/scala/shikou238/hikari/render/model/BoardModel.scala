package shikou238.hikari.render.model

import java.nio.FloatBuffer

import org.lwjgl.opengl.GL11.{GL_TRIANGLES, GL_UNSIGNED_INT, glDrawElements}
import shikou238.hikari.system.block.{BlockSide, North}
import shikou238.lwjgl.math.matrix.Matrix4f
import shikou238.lwjgl.render.buffer.model.AnimateModel
import shikou238.lwjgl.math.vector._
import shikou238.lwjgl.render.buffer.vertex.{ElementBuffer, StaticModelBuffer, Vertex}
import shikou238.lwjgl.render.shader.{Attribute, FragmentShader, ShaderProgram, VertexShader}

class BoardModel extends AnimateModel{
    case class BlockVertex(position: Vector3f, texcoord: Vector2f, normal : Vector3f) extends Vertex {
      override def put(buffer: FloatBuffer): Unit = {
        position.toBuffer(buffer)
        texcoord.toBuffer(buffer)
        normal.toBuffer(buffer)
      }

      override val size: Int = 8
    }


    val vertexShader = new VertexShader(
      """
        |#version 330 core
        |
        |in vec3 position;
        |in vec2 texcoord;
        |in vec3 normal;
        |
        |out vec3 vertexColor;
        |out vec2 tex;
        |out vec3 nor;
        |out vec4 pos;
        |
        |uniform mat4 side;
        |uniform mat4 model;
        |uniform mat4 world;
        |uniform mat4 view;
        |uniform mat4 projection;
        |
        |
        |void main(){
        |  mat4 mw = world * model * side;
        |  tex = texcoord;
        |  pos = mw * vec4(position, 1.0);
        |  nor = normalize(vec3(side * vec4(normal, 1.0)));
        |  vertexColor = vec3(1.0, 1.0, 1.0);
        |  gl_Position = projection * view * pos;
        |}
      """.stripMargin) {
      override val attributes: List[(CharSequence, Attribute.Attribute)] =
        List(
          ("position", Attribute.vec3),
          ("texcoord", Attribute.vec2),
          ("normal", Attribute.vec3)
        )
    }

    val fragmentShader = new FragmentShader(
      """
        |#version 330 core
        |
        |in vec3 vertexColor;
        |in vec2 tex;
        |in vec3 nor;
        |in vec4 pos;
        |
        |out vec4 fragColor;
        |
        |const vec3 light = normalize(vec3(1.0, 1.0, 1.0));
        |const float light_power = 1.0;
        |
        |uniform vec3 camera;
        |uniform sampler2D texImage;
        |
        |float safedot(const in vec3 a, const in vec3 b){
        |  return clamp(dot(a,b) , .0, 1.0);
        |}
        |float safedot(const in vec4 a, const in vec4 b){
        |  return clamp(dot(a,b) , .0, 1.0);
        |}
        |void main() {
        |  vec3 reflect = reflect(light, vec3(nor));
        |  vec3 v = normalize(camera - vec3(pos));
        |  float ambient = 0.1;
        |  float diffuse = safedot(nor, light) * 1.0;
        |  float specular = 2.0 * pow(safedot(reflect, v), 2.0);
        |  if(dot(nor, light) < 0.0 ){
        |    specular = 0.0;
        |  }
        |
        |  float final_light = ambient + diffuse * 1.0 + specular * 1.0;
        |  fragColor =
        |    vec4(vertexColor, 1.0) *
        |    texture(texImage, tex) *
        |    final_light;
        |}
      """.stripMargin) {
      override val output: (Int, CharSequence) = (0, "vertexColor")
    }

    val cube = new StaticModelBuffer[BlockVertex](
      BlockVertex(Vector3f(-0.5f, 0.5f, 0.5f), Vector2f(0f, 1f),Vector3f(0f, 0f, 1f)),
      BlockVertex(Vector3f( 0.5f, 0.5f, 0.5f), Vector2f(1f, 1f),Vector3f(0f, 0f, 1f)),
      BlockVertex(Vector3f(-0.5f,-0.5f, 0.5f), Vector2f(0f, 0f),Vector3f(0f, 0f, 1f)),
      BlockVertex(Vector3f( 0.5f,-0.5f, 0.5f), Vector2f(1f, 0f),Vector3f(0f, 0f, 1f)),
    )

    val vertexElement = new ElementBuffer(
      0,1,2,
      1,3,2,
    )

    val program = new ShaderProgram(vertexShader, fragmentShader)

    program.specifyAttributes(cube, vertexElement)

    var delta = 100f

    program("model") := Matrix4f.identity

    program("side") := Matrix4f.identity

    program("view") := Matrix4f.translate(0f,0f,-5f)

    program("projection") := Matrix4f.perspective(110f, 4f/3f, 0.0001f, 10)

    program("texImage") := 0

    program("camera") := Vector3f(0, 0, 0)

    position = (0,0,0)

    private[this] var p = (0,0,0)

    def position = p

    def position_= (value: (Int, Int, Int)): Unit ={
      p=value
      val (x,y,z) = value
      program("world") := Matrix4f.translate(x + .5f, y + .5f, z + .5f)
      program("world") :=
        Matrix4f(
          Matrix4f.translate(p._1 + .5f, p._2 + .5f, p._3 + .5f)
        )
    }
    private[this] var s: BlockSide  = North

    def side = s

    def side_=  (side: BlockSide): Unit ={
      s = side
      program("side") := side .rotate
    }

    override def update(): Unit = {
      delta += 10f

    }

    override def render(): Unit ={
      vertexShader.vao.beHot()
      program.beHot()
      glDrawElements(GL_TRIANGLES, 2 * 3, GL_UNSIGNED_INT, 0)
    }
  }