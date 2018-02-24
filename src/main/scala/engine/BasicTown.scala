package engine

import utils.Pos

class BasicTown(pos : Pos) extends Town(pos : Pos) {

  override def addStation(station: Station): Unit = {

  }

  override def produce(): Unit = {

  }

  override def unload(train : Train): Unit = {
    population += train.nbPassenger
  }

  override def load(train : Train, from : Station, to : Station, nbPassenger : Int): Unit = {
    from.load(train, to, nbPassenger)
  }

  override def explore(): Unit = {

  }

  override def step(): Unit = {

  }

  override def info(): String = {
    "Name : " + name + "\n\n"
  }
}
