package engine

import utils.Pos

class BasicStation(pos : Pos, town : Town) extends Station(pos : Pos, town : Town) {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }

  override def load(train: Train, objective: Station, nbPassenger : Int): Unit = {
    train.setObjective(objective, pos, nbPassenger)
  }

  override def unload(train : Train) : Unit = {
    town.unload(train)
  }

  override def info(): String = {
    "Name : " + name + "\n\n" +
    "Capacity : " + capacity + "\n\n"
  }

  override def getPos(): Pos = pos

}
