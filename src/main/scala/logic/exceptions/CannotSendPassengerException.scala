package logic.exceptions

object CannotSendPassengerException {
  def unapply(e: CannotSendPassengerException): Option[(String,Throwable)]
  = Some((e.getMessage, e.getCause))
}

class CannotSendPassengerException(nbPassenger : Int) extends Exception(nbPassenger.toString) {

}

