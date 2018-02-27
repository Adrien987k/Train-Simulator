package engine

object CannotBuildItemException {
  def unapply(e: CannotBuildItemException): Option[(String,Throwable)]
    = Some((e.getMessage, e.getCause))
}

class CannotBuildItemException(message : String) extends Exception(message) {

  /*def this(message: String, cause: Throwable) {
    this(message)
    initCause(cause)
  }

  def this(cause: Throwable) {
    this(Option(cause).map(_.toString).orNull, cause)
  }

  def this() {
    this(null: String)
  }*/

}
