package engine

import utils.Pos

import scala.collection.mutable.ListBuffer

class BasicStation(_name : String, _pos : Pos, town : Town) extends Station(_name : String, _pos : Pos, town : Town) {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }

  override def addRail(rail : Rail): Unit = {
    rails += rail
  }

  override def addTrain(train : Train): Boolean = {
    if (isFull) return false
    trains += train
    true
  }

  override def neighbours(): ListBuffer[Station] = {
    val neighbourList : ListBuffer[Station] = ListBuffer.empty
    for (rail <- rails) {
      if (rail.stationA == this) neighbourList += rail.stationB
      if (rail.stationB == this) neighbourList += rail.stationA
    }
    neighbourList
  }

  override def sendPassenger(objective : Station, nbPassenger : Int) : Unit = {
    if (trains.isEmpty) {
      waitingPassengers += nbPassenger
      return
    }
    getRailTo(objective) match {
      case Some(rail) =>
        if (rail.nbTrain == rail.DEFAULT_CAPACITY) {
          waitingPassengers += nbPassenger
          return
        }
        val train = trains.remove(0)
        load(train, objective, nbPassenger)
        rail.addTrain(train)

      case None =>
        waitingPassengers += nbPassenger
    }
  }

  override def getRailTo(station : Station) : Option[Rail] = {
    rails.find(rail => rail.stationA == station || rail.stationB == station)
  }

  override def load(train: Train, objective: Station, nbPassenger : Int): Unit = {
    train.setObjective(objective, pos, nbPassenger)
  }

  override def unload(train : Train) : Unit = {
    town.population += train.nbPassenger
  }

  override def info(): String = {
    "Name : " + name + "\n\n" +
    "Capacity : " + capacity + "\n\n"
  }

}
