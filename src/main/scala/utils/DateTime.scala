package utils

object DateTime {

  def sum(dateTimeA : DateTime, dateTimeB : DateTime) : DateTime = {
    val newDateTime : DateTime = new DateTime(
      dateTimeA.minutes + dateTimeB.minutes,
      dateTimeA.hours + dateTimeB.hours,
      dateTimeA.days + dateTimeB.days
    )

    newDateTime.normalize()

    newDateTime
  }

}

class DateTime
(var minutes : Int = 0,
 var hours : Int = 0,
 var days : Int = 1) extends Comparable[DateTime] {

  def update() : Unit = {
    minutes += 1
    if (minutes == 60) {
      minutes = 0
      hours += 1
      if (hours == 24) {
        hours = 0
        days += 1
      }
    }
  }

  def time() : String = {
    "Day : " + days + " -- time : " +
      (if (hours > 9) hours else "0" + hours) +
      ":" +
      (if (minutes > 9) minutes else "0" + minutes)
  }

  def restart() : Unit = {
    minutes = 0
    hours = 0
    days = 1
  }

  def normalize() : Unit = {
    if (minutes > 59) {
      hours += (minutes - 59) / 60
    }

    if (hours > 23) {
      days += (hours - 23) / 24
    }
  }

  override def compareTo(date : DateTime) : Int = {
    if ((days > date.days)
      || (days == date.days && hours > date.hours)
      || (minutes >= date.minutes && hours == date.hours && days == date.days))
      return 1

    0
  }

}
