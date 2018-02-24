package engine

import interface.GUI
import utils.Pos

class BasicTrain(pos : Pos) extends Train(pos : Pos) {

  override def place(): Unit = {

  }

  override def render(): Unit = {
    GUI.displayTrain(pos)
  }

  override def step(): Unit = {
    goalStation match {
      case Some(station) =>
        if (pos.inRange(station.getPos(), 10)) {
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
      dir.x = station.getPos().x - from.x
      dir.x = station.getPos().y - from.y

      this.nbPassenger = nbPassengers
  }

  override def info(): String = {
    ""
  }

  override def getPos(): Pos = pos

}
