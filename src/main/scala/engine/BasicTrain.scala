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
          //Arrivé
        } else {
          pos.x = pos.x + (dir.x * speed)
          pos.y = pos.y + (dir.y * speed)
        }
      case None =>
    }

    //
    render()
  }

  override def info(): String = ???

  override def getPos(): Pos = pos
}
