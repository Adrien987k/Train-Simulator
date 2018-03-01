package engine

import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicStation(_pos : Pos, town : Town) extends Station(_pos : Pos, town : Town) {

  override def place(): Unit = {

  }

  override def step(): Unit = {
    for ((station, nbPassenger) <- waitingPassengers) {
      val success = sendPassenger(station, nbPassenger)
      if (success) waitingPassengers -= station
    }
  }

  override def addRail(rail : Rail): Unit = {
    rails += rail
  }

  override def buildTrain(): Boolean = {
    if (isFull) return false
    val train = new BasicTrain(pos.copy())
    World.company.trains += train
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

  override def sendPassenger(objective : Station, nbPassenger : Int) : Boolean = {
    println("SEND PASSENGER")
    if (trains.isEmpty) {
      waitingPassengers += ((objective, nbPassenger))
      return false
    }
    getRailTo(objective) match {
      case Some(rail) =>
        if (rail.nbTrain == rail.DEFAULT_CAPACITY) {
          waitingPassengers += ((objective, nbPassenger))
          return false
        }
        println("REAL SEND")
        val train = trains.remove(0)
        load(train, objective, nbPassenger)
        train.putOnRail(rail)
      case None =>
        waitingPassengers += ((objective, nbPassenger))
        return false
    }
    true
  }

  override def getRailTo(station : Station) : Option[Rail] = {
    rails.find(rail => rail.stationA == station || rail.stationB == station)
  }

  override def load(train: Train, objective: Station, nbPassenger : Int): Unit = {
    train.setObjective(objective, pos, nbPassenger)
  }

  override def unload(train : Train) : Unit = {
    train.unsetObjective()
    trains += train
    town.population += train.nbPassenger
  }

  override def toString: String = {
    "Town : " + town.name + "\n\n" +
    "Capacity : " + capacity + "\n\n"
  }

  override def propertyPane(): Node = {
    val panel = new VBox()
    val stationLabel = new Label("=== Station ===")
    val capacityLabel = new Label("Capacity : " + capacity)
    val trainsLabel = new Label("Trains : " + trains.size)
    val waitingPassengerLabel = new Label("Waiting passenger : " + nbWaitingPassengers())
    panel.children = List(stationLabel, capacityLabel, trainsLabel, waitingPassengerLabel)
    panel
  }

}
