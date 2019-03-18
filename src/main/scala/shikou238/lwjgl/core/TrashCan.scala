package shikou238.lwjgl.core

object TrashCan { can =>
  private[this] var inCan = List.empty[FateOfTrash]
  private def planToTrash (o: FateOfTrash): Unit = {inCan ::= o}
  def cleanUp(): Unit = {
    inCan.foreach(_.trash())
    inCan = List.empty
  }
  trait FateOfTrash{
    can planToTrash this
    def trash(): Unit
  }
}
