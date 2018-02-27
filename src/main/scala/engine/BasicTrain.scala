package engine

import interface.GUI
import utils.Pos

class BasicTrain(_pos : Pos) extends Train(_pos : Pos) {

  override def place(): Unit = {

  }

  override def render(): Unit = {
    GUI.displayTrain(pos)
  }

  override def step(): Unit = {
    goalStation match {
      case Some(station) =>
        if (pos.inRange(station.pos, 10)) {
          station.unload(this)
        } else {
          pos.x = pos.x + (dir.x * speed)
          pos.y = pos.y + (dir.y * speed)
        }
      case None =>
    }

    //
    render()
  }

  override def setObjective(station: Station, from: Pos, nbPassengers : Int): Unit = {
      if (goalStation.nonEmpty) return

      goalStation = Some(station)
      dir.x = station.pos.x - from.x
      dir.x = station.pos.y - from.y

      this.nbPassenger = nbPassengers
  }

  override def info(): String = {
    "Speed : " + speed + "\n" +
    "Size : " + size + "\n" +
    "Max Weight : " + maxWeight + "\n" +
    "nbPassenger : " + nbPassenger + "\n" +
      (if (goalStation.isEmpty)  "No Goal Station"
       else "Goal Station : " + goalStation.get.town.name)
  }

  override def putOnRail(rail: Rail): Boolean = {
    if (rail.isFull) return false
    currentRail match {
      case Some(_) => return false
      case None =>
        rail.addTrain(this)
        currentRail = Some(rail)
    }
    true
  }

  override def removeFromRail(): Unit = {
    currentRail match {
      case Some(rail) =>
        rail.removeTrain(this)
        currentRail = None
      case None =>
    }
  }
}
