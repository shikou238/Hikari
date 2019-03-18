import scala.util.control.NonFatal

package object shikou238 {
  def using[T <: AutoCloseable, V](r: => T)(f: T => V): V = {
    val resource: T = r
    require(resource != null, "resource is null")
    var exception: Option[Throwable] = None
    try {
      f(resource)
    } catch {
      case NonFatal(e) =>
        exception = Some(e)
        throw e
    } finally {
      exception match {
        case Some(e) =>
          try {
            resource.close()
          } catch {
            case NonFatal(suppressed) =>
              e.addSuppressed(suppressed)
          }
        case None =>
          resource.close()
      }
    }
  }
  def debugln[A](a: A):A = {println(a); a}
  def debug[A](a: A):A = {print(a); a}
}
