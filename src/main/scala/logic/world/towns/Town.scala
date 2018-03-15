package logic.world.towns

import logic.{PointUpdatable, Updatable, UpdateRate}
import logic.economy.{Offer, Request}
import logic.exceptions.CannotBuildItemException
import logic.items.transport.facilities.{BasicStation, Station}
import logic.world.World
import utils.Pos

import scala.util.Random

abstract class Town(_pos : Pos, private var _name : String) extends PointUpdatable {

  updateRate(UpdateRate.TOWN_UPDATE)
  pos = _pos

  private val rand = new Random()

  val MAX_POPULATION = 1000000
  val MIN_POPULATION = 10
  val DEFAULT_PROPORTION_TRAVELER = 0.1

  var station : Option[Station] = None
  var offer : Offer = new Offer
  var request : Request = new Request

  var population : Int = MIN_POPULATION +
      new Random().nextInt(MAX_POPULATION + 1 - MIN_POPULATION)

  var proportionTraveler : Double = DEFAULT_PROPORTION_TRAVELER

  def name : String = _name
  def hasStation : Boolean = station.nonEmpty

  override def step(): Unit = {
    val traveler = (proportionTraveler * population / 100).toInt
    if (traveler != 0)
      sendPeopleToNeighbours(traveler)

    station.foreach(_.step())
  }

  def nbWaitingPassengers : Int =
    if (hasStation) station.get.nbWaitingPassengers() else 0

  def buildStation(): Unit = {
    station match {
      case Some(_) =>
        throw new CannotBuildItemException("This town already have a station")
      case None => station = Some(new BasicStation(World.company, pos, this))
    }
  }

  def buildTrain(): Unit = {
    if (!hasStation)
      throw new CannotBuildItemException("This town does not have a station")
    station.get.buildTrain()
  }

  def sendPeopleToNeighbours(nbPassenger : Int) : Unit = {
    val (nbNeighbour, neighbours) = station match {
      case Some(st) => st.nbNeighbours() -> st.neighbours()
      case None => return
    }

    if (nbNeighbour == 0) return

    val nbPersonPerNeighbour = nbPassenger / nbNeighbour
    val nbPersonPerNeighbourRest = nbPassenger % nbNeighbour
    val availableTrains = station.get.availableTrains()

    if (availableTrains < nbNeighbour) {
      val randPos = rand.nextInt(nbNeighbour)
      for (i <- 1 to availableTrains) {
        //TODO Change station to TransportFacility
        //sendPeople(neighbours((randPos + i) % nbNeighbour), nbPersonPerNeighbour)
      }
    } else {
      neighbours.foreach(neighbourStation =>
        println("TODO")
        //sendPeople(neighbourStation, nbPersonPerNeighbour)
      )
    }

    if (nbPersonPerNeighbourRest > 0) {
      val randNeigh = rand.nextInt(nbNeighbour)
      //sendPeople(neighbours(randNeigh), nbPersonPerNeighbourRest)
    }
  }

  def sendPeople(to : Station, nbPassenger : Int) : Unit =
    station.foreach(_.sendPassenger(to, nbPassenger))

  def explore()
  def produce()

}
