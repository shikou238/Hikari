package shikou238.hikari.system

import shikou238.hikari.system.math.Vector3i

abstract class Entity(var pos: Vector3i){
  def update(): Unit = {}
  def draw3d(): Unit
  def drawXZ(): Unit
  def drawXY(): Unit
}
