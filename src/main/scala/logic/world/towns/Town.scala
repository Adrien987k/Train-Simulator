package logic.world.towns

import game.Game
import logic.{PointUpdatable, UpdateRate}
import logic.economy.{Offer, Request}
import logic.exceptions.CannotBuildItemException
import logic.items.ItemTypes._
import logic.items.transport.facilities._
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
  var airport : Option[Airport] = None

  var offer : Offer = new Offer
  var request : Request = new Request

  var population : Int = MIN_POPULATION +
      new Random().nextInt(MAX_POPULATION + 1 - MIN_POPULATION)

  var proportionTraveler : Double = DEFAULT_PROPORTION_TRAVELER

  def name : String = _name
  def hasStation : Boolean = station.nonEmpty
  def hasAirport : Boolean = airport.nonEmpty

  override def step(): Unit = {
    val traveler = (proportionTraveler * population / 100).toInt
    if (traveler != 0)
      sendPeopleToNeighbours(traveler)

    station.foreach(_.step())
    airport.foreach(_.step())
  }

  /**
    * @return the number of current waiting passengers
    */
  def nbWaitingPassengers : Int = {
    var waiters = 0
    waiters += (if (hasStation) station.get.nbWaitingPassengers() else 0)
    waiters += (if (hasAirport) airport.get.nbWaitingPassengers() else 0)
    waiters
  }

  /**
    * Try to build a transport facility of type [tfType]
    * If it already exists, display an error message to the user
    *
    * @param tfType the transportFacility type
    */
  def buildTransportFacility(tfType : TransportFacilityType) : Unit = {
    def buildInternal(tf : Option[TransportFacility], itemNameForError : String) : TransportFacility = {
      tf match {
        case Some(_) =>
          throw new CannotBuildItemException("This town already have a " + itemNameForError)
        case None =>
          TransportFacilityFactory.make(tfType, Game.world.company, this)
      }
    }
    tfType match {
      case STATION => station = Some(buildInternal(station, "a station").asInstanceOf[Station])
      case AIRPORT => airport = Some(buildInternal(airport, "an airport").asInstanceOf[Airport])
    }
  }

  /**
    * Build a new vehicle of type [vehicleType] and add it to the appropriate
    * transport facility of this town if it exists. If not, display a warning
    * message to the player
    *
    * @param vehicleType The type of vehicle to build
    */
  def buildVehicle(vehicleType : VehicleType) : Unit = {
    vehicleType match {
      case DIESEL_TRAIN | ELECTRIC_TRAIN =>
        if (!hasStation) throw new CannotBuildItemException("This town does not have a Station")
        station.get.buildVehicle(vehicleType)

      case BOEING | CONCORDE =>
        if (!hasAirport) throw new CannotBuildItemException("This town does not have an airport")
        airport.get.buildVehicle(vehicleType)
    }
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
