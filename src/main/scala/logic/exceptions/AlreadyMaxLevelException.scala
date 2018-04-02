package logic.exceptions

object AlreadyMaxLevelException {
  def unapply(e: CannotBuildItemException): Option[(String,Throwable)]
  = Some((e.getMessage, e.getCause))
}

class AlreadyMaxLevelException(message : String) extends Exception(message) {

}

