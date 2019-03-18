package shikou238.hikari.system

import shikou238.hikari.system.block._

import shikou238.hikari.gui.{RenderMode, XYOrthoMode, PersepectiveMode, XZOrthoMode}

import shikou238.hikari.system.math.Vector3i

class World(val size: Vector3i){
  val map = new BlockBuffer(size)
}
class BlockBuffer(size: Vector3i){
  val X = size.x
  val Y = size.y
  val Z = size.z

  val a:Array[BlockFactor] = Array.fill[BlockFactor](X*Y*Z)(new BlockFactor(Air))

  def drawBlock3D(x: Int, y:Int, z: Int){
    val shouldDraw = collection.mutable.ListBuffer.empty[BlockSide]

    val b = apply(x,y,z)
    val f = currentBlock

    f.north match {
      case None => shouldDraw += North
      case Some(b) =>
        if(b.b == Air)
          shouldDraw += North
    }
    f.south match {
      case None => shouldDraw += South
      case Some(b) =>
        if(b.b == Air)
          shouldDraw += South
    }
    f.top match {
      case None => shouldDraw += Top
      case Some(b) =>
        if(b.b == Air)
          shouldDraw += Top
    }
    f.bottom match {
      case None => shouldDraw += Bottom
      case Some(b) =>
        if(b.b == Air)
          shouldDraw += Bottom
    }
    f.west match {
      case None => shouldDraw += West
      case Some(b) =>
        if(b.b == Air)
          shouldDraw += West
    }
    f.east match {
      case None => shouldDraw += East
      case Some(b) =>
        if(b.b == Air)
          shouldDraw += East
    }

    b.draw3d(Vector3i(x, y, z), shouldDraw: _*)
  }
  def drawBlock(x: Int, y:Int, z: Int, mode: RenderMode): Unit ={
    mode match {
      case PersepectiveMode =>
        drawBlock3D(x,y,z)
      case XYOrthoMode =>
        apply(x,y,z).drawXY(Vector3i(x,y,z))
      case XZOrthoMode =>
        apply(x,y,z).drawXZ(Vector3i(x,y,z))
    }
  }
  //  def draw(): Unit = draw3d(Vector3i(0, 0, 0), Vector3i(X-1, Y-1, Z-1))
  def draw(from: Vector3i, to: Vector3i, mode: RenderMode): Unit ={
    val Vector3i(fromX, fromY, fromZ) = from
    val Vector3i(toX,toY,toZ) = to

    apply(fromX, fromY, fromZ)
    var b = currentBlock
    var zBack,yBack = false
    for(x <- fromX to toX){
      if(yBack){
        for(y <- fromY to toY) {
          if (zBack) {
            for (z <- toZ.to (fromZ, -1)) {
              drawBlock(x,y,z, mode)
            }
            zBack = false
          }
          else {
            for (z <- fromZ to toZ) {
              drawBlock(x,y,z, mode)
            }
            zBack = true
          }
        }
        yBack = true
      }
      else{
        for(y <- fromY to toY) {
          if (zBack) {
            for (z <- fromZ to toZ) {
              drawBlock(x,y,z, mode)
            }
            zBack = false
          }
          else {
            for (z <- fromZ to toZ) {
              drawBlock(x,y,z, mode)
            }
            zBack = true
          }
        }
        yBack = true
      }
    }
  }

  for{
    x <- Range(0, X)
    y <- Range(0, Y)
    z <- Range(0, Z)
  }{
    def find(x: Int, y: Int, z: Int): BlockFactor ={
      a(x*Y*Z + y*Z + z)
    }
    val bf = find(x,y,z)
    bf.pos = Vector3i(z,y,z)
    if(x > 0){
      val prex = find(x-1,y,z)
      bf.east = Some(prex)
      prex.west = Some(bf)
    }
    if(y > 0){
      val prey = find(x,y-1,z)
      bf.bottom = Some(prey)
      prey.top = Some(bf)
    }
    if(z > 0){
      val prez = find(x,y,z-1)
      bf.south = Some(prez)
      prez.north = Some(bf)
    }
  }

  var currentBlock = a(0)
  var currentPos = (0,0,0)


  def moveNextX = {
    currentBlock = (currentBlock.west: @unchecked) match {
      case Some(f) => f
    }
    currentPos = (currentPos._1+1,currentPos._2+0,currentPos._3+0)
  }
  def movePrevX = {
    currentBlock = (currentBlock.east: @unchecked) match {
      case Some(f) => f
    }
    currentPos = (currentPos._1-1,currentPos._2+0,currentPos._3+0)
  }
  def moveNextY = {
    currentBlock = (currentBlock.top: @unchecked) match {
      case Some(f) => f
    }
    currentPos = (currentPos._1+0,currentPos._2+1,currentPos._3+0)
  }
  def movePrevY = {
    currentBlock = (currentBlock.bottom: @unchecked) match {
      case Some(f) => f
    }
    currentPos = (currentPos._1+0,currentPos._2-1,currentPos._3+0)
  }
  def moveNextZ = {
    currentBlock = (currentBlock.north: @unchecked) match {
      case Some(f) => f
    }
    currentPos = (currentPos._1+0,currentPos._2+0,currentPos._3+1)
  }
  def movePrevZ = {
    currentBlock = (currentBlock.south: @unchecked) match {
      case Some(f) => f
    }
    currentPos = (currentPos._1+0,currentPos._2+0,currentPos._3-1)
  }

  def apply(x: Int, y: Int, z: Int): Block ={
    while(
      currentPos._1 != x ||
        currentPos._2 != y ||
        currentPos._3 != z
    ){
      if(currentPos._1 > x)
        movePrevX
      if(currentPos._1 < x)
        moveNextX
      if(currentPos._2 > y)
        movePrevY
      if(currentPos._2 < y)
        moveNextY
      if(currentPos._3 > z)
        movePrevZ
      if(currentPos._3 < z)
        moveNextZ
    }
    currentBlock.b
  }

  def set(b: Block,x: Int, y: Int, z: Int): Unit ={
    val f = new BlockFactor(b)

    apply(x,y,z)
    val before = currentBlock

    before.east match {
      case Some(a) =>
        f.east = Some(a)
        a.west = Some(f)
      case None =>
    }
    before.west match {
      case Some(a) =>
        f.west = Some(a)
        a.east = Some(f)
      case None =>
    }
    before.bottom match {
      case Some(a) =>
        f.bottom = Some(a)
        a.top = Some(f)
      case None =>
    }
    before.top match {
      case Some(a) =>
        f.top = Some(a)
        a.bottom = Some(f)
      case None =>
    }
    before.south match {
      case Some(a) =>
        f.south = Some(a)
        a.north = Some(f)
      case None =>
    }
    before.north match {
      case Some(a) =>
        f.north = Some(a)
        a.south = Some(f)
      case None =>
    }
    currentBlock = f
  }

  class BlockFactor(val b: Block, var pos: Vector3i = Vector3i(0,0,0),
                    var east: Option[BlockFactor]=None, var west: Option[BlockFactor]=None,
                    var bottom: Option[BlockFactor]=None, var top: Option[BlockFactor]=None,
                    var south: Option[BlockFactor]=None, var north: Option[BlockFactor]=None,
                   ){
    def neighbors = List(east, west, bottom, top, south, north)

  }
}

