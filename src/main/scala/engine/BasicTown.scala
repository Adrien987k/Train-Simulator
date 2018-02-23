package engine

import utils.Pos

class BasicTown(pos : Pos) extends Town(pos : Pos) {

  override def addStation(station: Station): Unit = {

  }

  override def produce(): Unit = {

  }

  override def unload(): Unit = {

  }

  override def load(): Unit = {

  }

  override def explore(): Unit = {

  }

  override def step(): Unit = {

  }

  override def info(): String = {
    "Name : " + name + "\n\n"
  }
}
