package engine

import utils.Pos

import scala.util.Random

class BasicTown(pos : Pos, name : String) extends Town(pos : Pos, name : String) {

  override def step(): Unit = {

  }

  override def addStation(name : String): Unit = {
    stations += new BasicStation(name, pos, this)
  }

  override def sendPeopleToNeighbours(number: Int): Unit = {
    var nbNeighbour = 0
    for (station <- stations) {
      nbNeighbour += station.nbNeighbours()
    }

    if (nbNeighbour == 0) return

    val nbPersonPerNeighbour = number / nbNeighbour
    val nbPersonPerNeighbourRest = number % nbNeighbour

    for (station <- stations) {
      for (neighbourStation <- station.neighbours()) {
        sendPeople(station, neighbourStation, nbPersonPerNeighbour)
      }
    }
    val rand = new Random()
    val randStation = rand.nextInt(stations.size)
    val randNeigh = rand.nextInt(stations(randStation).nbNeighbours())

    val otherStations = stations(randStation).neighbours()
    sendPeople(stations(randStation), otherStations(randNeigh), nbPersonPerNeighbourRest)
  }

  override def sendPeople(from : Station, to : Station, nbPassenger : Int): Unit = {
    from.sendPassenger(to, nbPassenger)
  }

  override def explore(): Unit = {

  }

  override def produce(): Unit = {

  }

  override def info(): String = {
    "Name : " + name + "\n\n"
  }

}
