package utils

object DateTime {

  def sum(dateTimeA : DateTime, dateTimeB : DateTime) : DateTime = {
    val newDateTime : DateTime = new DateTime(
      dateTimeA.hours + dateTimeB.hours,
      dateTimeA.days + dateTimeB.days
    )

    newDateTime.normalize()

    newDateTime
  }

}

class DateTime
(var hours : Int = 0,
 var days : Int = 1) extends Comparable[DateTime] {

  def update() : Unit = {
    hours += 1
    if (hours == 24) {
      hours = 0
      days += 1
    }
  }

  def time() : String = {
    "Day : " + days + " -- time : " +
      (if (hours > 9) hours else "0" + hours)
  }

  def restart() : Unit = {
    hours = 0
    days = 1
  }

  def normalize() : Unit = {
    if (hours > 23) {
      days += (hours - 23) / 24
    }
  }

  override def compareTo(date : DateTime) : Int = {
    if ((days > date.days)
      || (days == date.days && hours > date.hours))
      return 1

    0
  }

}
