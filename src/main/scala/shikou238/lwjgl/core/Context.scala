package shikou238.lwjgl.core

object Context {
  val trend = collection.mutable.Map.empty[String , Fashonable]

  trait Fashonable{
    val indentifier: String
    final def beHot(): Unit ={
      if(!trend.keys.exists(_ == indentifier) || trend(indentifier) != this){
        hot()
        trend(indentifier) = this
      }
    }
    protected def hot(): Unit
  }
}
