package engine

import utils.Pos

import scala.util.Random

class BasicTown(pos : Pos, name : String) extends Town(pos : Pos, name : String) {

  var counter = 0

  override def step(): Unit = {
    counter += 1
    if (counter == 100) {
      counter = 0
      val traveler = proportionTraveler * population / 100
      if (traveler != 0)
        sendPeopleToNeighbours(traveler)

      station.foreach(_.step())
    }
  }

  override def buildStation(): Unit = {
    station match {
      case Some(_) =>
        throw new CannotBuildItemException("This town already have a station")
      case None => station = Some (new BasicStation(pos, this))
    }
  }

  override def buildTrain(): Unit = {
    if (!hasStation)
      throw new CannotBuildItemException("This town does not have a station")
    station.get.buildTrain()
  }

  override def sendPeopleToNeighbours(nbPassenger: Int): Unit = {
    var (nbNeighbour, neighbours) = station match {
      case Some(st) => st.nbNeighbours() -> st.neighbours()
      case None => return
    }
    if (nbNeighbour == 0) return

    val nbPersonPerNeighbour = nbPassenger / nbNeighbour
    val nbPersonPerNeighbourRest = nbPassenger % nbNeighbour

    neighbours.foreach(neighbourStation =>
      sendPeople(neighbourStation, nbPersonPerNeighbour)
    )

    val randNeigh = new Random().nextInt(nbNeighbour)
    sendPeople(neighbours(randNeigh), nbPersonPerNeighbourRest)
  }

  override def sendPeople(to : Station, nbPassenger : Int): Unit = {
    station.foreach(_.sendPassenger(to, nbPassenger))
  }

  override def explore(): Unit = {

  }

  override def produce(): Unit = {

  }

  override def info(): String = {
    "Name : " + name + "\n\n"
  }

}
