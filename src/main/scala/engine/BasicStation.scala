package engine

import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicStation(company: Company, _pos : Pos, town : Town) extends Station(company: Company, _pos : Pos, town : Town) {

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
    val train = new BasicTrain(company, pos.copy())
    company.trains += train
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
        val train = trains.remove(0)
        load(train, objective, nbPassenger)
        val success = train.putOnRail(rail)
        if (success) {
          company.money += nbPassenger * rail.length * company.ticketPricePerKm
        } else {
          unload(train)
          return false
        }
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
    town.population -= nbPassenger
    train.setObjective(objective, pos)
    train.nbPassenger = nbPassenger
  }

  override def unload(train : Train) : Unit = {
    town.population += train.nbPassenger
    train.unsetObjective()
    train.nbPassenger = 0
    trains += train
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
