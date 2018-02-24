package engine

import utils.Pos

class BasicRail(pos1 : Pos, pos2 : Pos) extends Rail(pos1 : Pos, pos2 : Pos) {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }

  override def info(): String = {
    "Max capacity : " + DEFAULT_CAPACITY + "\n\n" +
    "Trains : " + currentNbTrains
  }

  override def getPos(): Pos = pos1
}
