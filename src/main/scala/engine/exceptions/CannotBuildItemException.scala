package engine.exceptions

object CannotBuildItemException {
  def unapply(e: CannotBuildItemException): Option[(String,Throwable)]
    = Some((e.getMessage, e.getCause))
}

class CannotBuildItemException(message : String) extends Exception(message) {

}
