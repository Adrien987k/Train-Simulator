package engine

import utils.Pos

import scala.util.Random
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, VBox}

class BasicTown(pos : Pos, name : String) extends Town(pos : Pos, name : String) {

  private val rand = new Random()
  private var counter = rand.nextInt(UpdateRate.TOWN_UPDATE)

  override def step(): Unit = {
    counter += 1
    if (counter == UpdateRate.TOWN_UPDATE) {
      counter = 0
      val traveler = (proportionTraveler * population / 100).toInt
      if (traveler != 0)
        sendPeopleToNeighbours(traveler)

      station.foreach(_.step())
    }
  }

  override def buildStation(): Unit = {
    station match {
      case Some(_) =>
        throw new CannotBuildItemException("This town already have a station")
      case None => station = Some(new BasicStation(World.company, pos, this))
    }
  }

  override def buildTrain(): Unit = {
    if (!hasStation)
      throw new CannotBuildItemException("This town does not have a station")
    station.get.buildTrain()
  }

  override def sendPeopleToNeighbours(nbPassenger: Int): Unit = {
    val (nbNeighbour, neighbours) = station match {
      case Some(st) => st.nbNeighbours() -> st.neighbours()
      case None => return
    }

    if (nbNeighbour == 0) return

    val nbPersonPerNeighbour = nbPassenger / nbNeighbour
    val nbPersonPerNeighbourRest = nbPassenger % nbNeighbour
    val availableTrains = station.get.availableTrains

    if (availableTrains < nbNeighbour) {
      val randPos = rand.nextInt(nbNeighbour)
      for (i <- 1 to availableTrains) {
        sendPeople(neighbours((randPos + i) % nbNeighbour), nbPersonPerNeighbour)
      }
    } else {
      neighbours.foreach(neighbourStation =>
        sendPeople(neighbourStation, nbPersonPerNeighbour)
      )
    }

    if (nbPersonPerNeighbourRest > 0) {
      val randNeigh = rand.nextInt(nbNeighbour)
      sendPeople(neighbours(randNeigh), nbPersonPerNeighbourRest)
    }

  }

  override def sendPeople(to : Station, nbPassenger : Int): Unit = {
    station.foreach(_.sendPassenger(to, nbPassenger))
  }

  override def explore(): Unit = {

  }

  override def produce(): Unit = {

  }

  override def propertyPane(): Node = {
    val mainPane = new BorderPane
    val panel = new VBox()
    val nameLabel = new Label("=== " + name + " ===")
    val hasStationLabel = new Label(if (hasStation) "This town has a station"
    else "This does not have a station")
    val populationLabel = new Label("Population : " + population)
    val propTravelerLabel = new Label("Proportion of traveler : " + proportionTraveler)
    val posLabel = new Label("position : " + pos)
    panel.children = List(nameLabel, hasStationLabel, populationLabel, propTravelerLabel, posLabel, new Label("\n"))
    mainPane.top = panel
    if (hasStation)
      mainPane.center = station.get.propertyPane()
    mainPane
  }

  override def toString: String = {
    "Name : " + name + "\n\n"
  }

}
