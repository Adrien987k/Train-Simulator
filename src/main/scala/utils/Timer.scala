package utils

class Timer {

  var minutes = 0
  var hours = 0
  var days = 1

  def update(): Unit = {
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

  def time(): String = {
    "Day : " + days + " -- time : " +
      (if (hours > 9) hours else "0" + hours) +
      ":" +
      (if (minutes > 9) minutes else "0" + minutes)
  }

  def restart(): Unit = {
    minutes = 0
    hours = 0
    days = 1
  }

}
