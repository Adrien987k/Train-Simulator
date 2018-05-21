package utils

object DateTime {

  def sum(dateTimeA : DateTime, dateTimeB : DateTime) : DateTime = {
    val newDateTime : DateTime = new DateTime(
      dateTimeA.days + dateTimeB.days,
      dateTimeA.hours + dateTimeB.hours
    )

    newDateTime.normalize()

    newDateTime
  }

}

class DateTime
(var days : Int = 1,
 var hours : Int = 0)
  extends Comparable[DateTime] {

  def update() : Unit = {
    hours += 1
    if (hours == 24) {
      hours = 0
      days += 1
    }
  }

  def time() : String = {
    "Day : " + days + " - Hour : " +
      (if (hours > 9) hours else "0" + hours)
  }

  override def toString: String = time()

  def restart() : Unit = {
    hours = 0
    days = 1
  }

  def load(node : scala.xml.NodeSeq) : Unit = {
    hours = (node \"@hours").text.toInt
    days = (node \"@days").text.toInt
  }

  def normalize() : Unit = {
    if (hours > 23) {
      days += (hours - 23) / 24
    }
  }

  def copy() : DateTime = {
    new DateTime(days, hours)
  }

  override def compareTo(date : DateTime) : Int = {
    if ((days < date.days) || (days == date.days && hours < date.hours))
      return 0

    1
  }

}
