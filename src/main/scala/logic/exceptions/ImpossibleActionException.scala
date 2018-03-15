package logic.exceptions

object ImpossibleActionException {
  def unapply(e: ImpossibleActionException): Option[(String,Throwable)]
  = Some((e.getMessage, e.getCause))
}

class ImpossibleActionException(message : String) extends Exception(message) {

}