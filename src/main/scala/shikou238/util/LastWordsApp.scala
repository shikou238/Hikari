package shikou238.util

trait LastWordsApp extends App{ // TODO should be tested.
  def fanfare(): Unit = {}
  def lastWords(): Unit

  override def main(args: Array[String]): Unit = {
    fanfare()
    super.main(args)
    lastWords()
  }
}
