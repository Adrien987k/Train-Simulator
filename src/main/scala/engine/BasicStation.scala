package engine

import utils.Pos

class BasicStation(pos : Pos) extends Station(pos : Pos) {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }

  override def info(): String = {
    "Name : " + name + "\n\n" +
    "Capacity : " + capacity + "\n\n"
  }

  override def getPos(): Pos = pos
}
