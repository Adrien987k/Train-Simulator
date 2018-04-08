package logic.world.towns

import game.Game
import logic.{PointUpdatable, UpdateRate}
import logic.economy.{Offer, Request}
import logic.exceptions.CannotBuildItemException
import logic.items.ItemTypes._
import logic.items.transport.facilities._
import utils.Pos

import scala.util.Random
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, VBox}

class Town(_pos : Pos, private var _name : String) extends PointUpdatable {

  updateRate(UpdateRate.TOWN_UPDATE)
  pos = _pos

  private val rand = new Random()

  val MAX_POPULATION = 1000000
  val MIN_POPULATION = 10
  val DEFAULT_PROPORTION_TRAVELER = 0.1

  var station : Option[Station] = None
  var airport : Option[Airport] = None
  var harbor : Option[Harbor] = None
  var gasStation : Option[GasStation] = None

  var offer : Offer = new Offer
  var request : Request = new Request

  var population : Int = MIN_POPULATION +
      new Random().nextInt(MAX_POPULATION + 1 - MIN_POPULATION)

  var proportionTraveler : Double = DEFAULT_PROPORTION_TRAVELER

  def name : String = _name

  def hasStation : Boolean = station.nonEmpty
  def hasAirport : Boolean = airport.nonEmpty
  def hasHarbor : Boolean = harbor.nonEmpty
  def hasGasStation : Boolean = gasStation.nonEmpty

  override def step() : Boolean = {
    if(!super.step()) return false

    val traveler = (proportionTraveler * population / 100).toInt
    if (traveler != 0)
      sendPeopleToNeighbours(traveler)

    station.foreach(_.step())
    airport.foreach(_.step())
    harbor.foreach(_.step())
    gasStation.foreach(_.step())

    true
  }

  /**
    * @param transportFacilityType The transport facility type to check
    * @return True if this station own a transport facility of type [transportFacilityType]
    */
  def hasTransportFacilityOfType(transportFacilityType : TransportFacilityType) : Boolean = {
    transportFacilityType match {
      case STATION => hasStation
      case AIRPORT => hasAirport
      case HARBOR => hasHarbor
      case GAS_STATION => hasGasStation
    }
  }

  /**
    * @return the number of current waiting passengers
    */
  def nbWaitingPassengers : Int = {
    var waiters = 0

    waiters += (if (hasStation) station.get.nbWaitingPassengers() else 0)
    waiters += (if (hasAirport) airport.get.nbWaitingPassengers() else 0)
    waiters += (if (hasHarbor) harbor.get.nbWaitingPassengers() else 0)
    waiters += (if (hasGasStation) gasStation.get.nbWaitingPassengers() else 0)

    waiters
  }

  /**
    * Try to build a transport facility of type [tfType]
    * If it already exists, display an error message to the user
    *
    * @param tfType the transportFacility type
    */
  def buildTransportFacility(tfType : TransportFacilityType) : Unit = {
    def buildInternal(tfOpt : Option[TransportFacility], itemNameForError : String) : TransportFacility = {
      tfOpt match {
        case Some(_) =>
          throw new CannotBuildItemException("This town already have " + itemNameForError)

        case None =>
          val tf = TransportFacilityFactory.make(tfType, Game.world.company, this)
          Game.world.company.addTransportFacility(tf)
          tf
      }
    }

    tfType match {
      case STATION => station = Some(buildInternal(station, "a station").asInstanceOf[Station])

      case AIRPORT => airport = Some(buildInternal(airport, "an airport").asInstanceOf[Airport])

      case HARBOR => harbor = Some(buildInternal(harbor, "an harbor").asInstanceOf[Harbor])

      case GAS_STATION => gasStation = Some(buildInternal(gasStation, "a gas station").asInstanceOf[GasStation])
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
        station.get.buildTrain(vehicleType.asInstanceOf[TrainType])

      case BOEING | CONCORDE =>
        if (!hasAirport) throw new CannotBuildItemException("This town does not have an airport")
        airport.get.buildPlane(vehicleType.asInstanceOf[PlaneType])

      case LINER | CRUISE_BOAT =>
        if (!hasHarbor) throw new CannotBuildItemException("This town does not have an harbor")
        harbor.get.buildShip(vehicleType.asInstanceOf[ShipType])

      case TRUCK =>
        if (!hasGasStation) throw new CannotBuildItemException("This town does not have a gas station")
        gasStation.get.buildTruck(vehicleType.asInstanceOf[TruckType])
    }
  }

  private def sendPeopleToNeighboursBy(nbPassenger : Int, tfOpt : Option[TransportFacility]) : Unit = {
    val (nbNeighbour, neighbours) = tfOpt match {
      case Some(tf) => tf.nbNeighbours() -> tf.neighbours()
      case None => return
    }

    if (nbNeighbour == 0) return

    val nbPersonPerNeighbour = nbPassenger / nbNeighbour
    val nbPersonPerNeighbourRest = nbPassenger % nbNeighbour
    val availableVehicles = tfOpt.get.availableVehicles

    if (availableVehicles < nbNeighbour) {
      val randPos = rand.nextInt(nbNeighbour)
      for (i <- 1 to availableVehicles) {
        sendPeople(tfOpt, neighbours((randPos + i) % nbNeighbour), nbPersonPerNeighbour)
      }
    } else {
      neighbours.foreach(neighbourStation =>
        sendPeople(tfOpt, neighbourStation, nbPersonPerNeighbour)
      )
    }

    if (nbPersonPerNeighbourRest > 0) {
      val randNeigh = rand.nextInt(nbNeighbour)
      sendPeople(tfOpt, neighbours(randNeigh), nbPersonPerNeighbourRest)
    }
  }

  private def sendPeople(from : Option[TransportFacility], to : TransportFacility, nbPassenger : Int) : Unit = {
    from.foreach(_.trySendPassenger(to, nbPassenger))
  }

  /**
    * Try to send as much as possible passengers to all the neighbours
    *
    * @param nbPassenger The number of passenger to send
    */
  def sendPeopleToNeighbours(nbPassenger : Int) : Unit = {
    var toStation = 0
    var toAirport = 0

    if (nbPassenger % 2 == 0) {
      toStation = nbPassenger / 2
      toAirport = nbPassenger / 2
    } else {
      toAirport = nbPassenger / 2
      toStation = (nbPassenger / 2) + 1
    }

    sendPeopleToNeighboursBy(toStation, station)
    sendPeopleToNeighboursBy(toAirport, airport)
  }

  /**
    * @return The transport facility associate with the type [transportFacilityType]
    */
  def transportFacilityOfType(transportFacilityType : TransportFacilityType) : Option[TransportFacility] = {
    transportFacilityType match {
      case STATION => station
      case AIRPORT => airport
      case HARBOR => harbor
      case GAS_STATION => gasStation
    }
  }

  def transportFacilityForVehicleType(vehicleType : VehicleType) : Option[TransportFacility] = {
    vehicleType match {
      case _ : TrainType => station
      case _ : PlaneType => airport
      case _ : ShipType => harbor
      case _ : TruckType => gasStation
    }
  }

  def explore() : Unit = {

  }

  def produce() : Unit = {

  }

  private val mainPanel = new BorderPane

  private val townPanel = new VBox()

  private val nameLabel = new Label("=== " + name + " ===")
  private val populationLabel = new Label()
  private val propTravelerLabel = new Label()
  private val posLabel = new Label()

  labels = List(nameLabel,
    populationLabel,
    propTravelerLabel,
    posLabel,
    new Label("\n"))

  styleLabels()

  townPanel.children = labels

  mainPanel.top = townPanel

  private var resized = false

  private val transportFacilitiesVBox = new VBox()

  mainPanel.center = transportFacilitiesVBox

  override def propertyPane() : Node = {
    populationLabel.text  = "Population : " + population
    propTravelerLabel.text = "Proportion of traveler : " + proportionTraveler
    posLabel.text = "position : " + pos

    if (hasStation && !transportFacilitiesVBox.children.contains(station.get.propertyPane()))
      transportFacilitiesVBox.children.add(station.get.propertyPane())

    if (hasAirport && !transportFacilitiesVBox.children.contains(airport.get.propertyPane()))
      transportFacilitiesVBox.children.add(airport.get.propertyPane())

    if (hasHarbor && !transportFacilitiesVBox.children.contains(harbor.get.propertyPane()))
      transportFacilitiesVBox.children.add(harbor.get.propertyPane())

    if (hasGasStation && !transportFacilitiesVBox.children.contains(gasStation.get.propertyPane()))
      transportFacilitiesVBox.children.add(gasStation.get.propertyPane())

    if (transportFacilitiesVBox.children.size() >= 4 && !resized) {
      styleLabels(14)
      station.get.styleLabels(14)
      airport.get.styleLabels(14)
      gasStation.get.styleLabels(14)
      harbor.get.styleLabels(14)

      resized = true
    }

    mainPanel
  }

}
