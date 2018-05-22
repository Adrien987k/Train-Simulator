package logic.items.transport.facilities

import logic.economy.Cargo
import logic.items.{EvolutionPlan, Facility}
import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.Road
import logic.items.transport.vehicules.VehicleTypes.VehicleType
import logic.items.transport.vehicules.{Vehicle, VehicleFactory}
import logic.world.{Company, Contract}
import logic.world.towns.Town
import utils.{Failure, Result, Success}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label

case class TransportFacilityEvolutionPlan
(capacities : List[Double],
 override val basePrice : Double,
 override val pricePerLevel : Double
) extends EvolutionPlan(
  List(capacities), basePrice, pricePerLevel
)

abstract class TransportFacility
(val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 override val evolutionPlan : TransportFacilityEvolutionPlan)
  extends Facility(transportFacilityType, company, town, evolutionPlan) {

  private var _capacity : Int = evolutionPlan.level(level).head.toInt

  private var waitingPassengers : mutable.HashMap[TransportFacility, Int] = mutable.HashMap.empty

  private val vehicles : ListBuffer[Vehicle] = ListBuffer.empty
  private val roads : ListBuffer[Road] = ListBuffer.empty

  private val waitingVehicles : ListBuffer[Vehicle] = ListBuffer.empty

  def capacity : Int = _capacity

  /**
    * @return True if vehicle capacity is reached
    */
  def isFull : Boolean = vehicles.lengthCompare(capacity) == 0

  def nbNeighbours() : Int = roads.size

  /**
    * @return The number of available vehicles
    */
  def availableVehicles : Int = vehicles.size

  /**
    * Connect this transport facility to the [road]
    *
    * @param road The road to connect
    */
  def connectRoad(road : Road) : Unit = {
    roads += road
  }

  override def step() : Boolean = {
    if(!super.step()) return false

    for ((transportFacility, nbPassenger) <- waitingPassengers) {
      trySendPassenger(transportFacility, nbPassenger)
    }

    vehicles.foreach(vehicle => {
      if (vehicle.destination.nonEmpty && !waitingVehicles.contains(vehicle))
        waitingVehicles += vehicle
    })

    waitingVehicles.foreach(vehicle => {
      sendToDestination(vehicle)
    })

    true
  }

  /**
    * Build a new vehicle of type [vehicleType]
    *
    * @param vehicleType the type of vehicle to build
    */
  protected def buildVehicle(vehicleType : VehicleType) : Result = {
    if (isFull) Failure("This facility is full")

    val vehicle = VehicleFactory.make(vehicleType, company, this)

    company.addVehicle(vehicle)

    vehicles += vehicle

    stats.newEvent(vehicleType.name + " built")

    Success()
  }

  /**
    * @return Number of passenger waiting for a specific destination
    */
  def nbWaitingPassengers() : Int = {
    waitingPassengers.foldLeft(0) {
      case (total, (_, nb)) => total + nb
    }
  }

  /**
    * @return All the neighbours off this transport facility
    */
  def neighbours() : ListBuffer[TransportFacility] = {
    val neighbourList : ListBuffer[TransportFacility] = ListBuffer.empty
    for (road <- roads) {
      if (road.transportFacilityA == this) neighbourList += road.transportFacilityB
      if (road.transportFacilityB == this) neighbourList += road.transportFacilityA
    }
    neighbourList
  }

  /**
    * Try to send @nbPassenger to @objective
    *
    * @param objective The transport facility objective
    * @param nbPassenger The number of passenger to send
    */
  def trySendPassenger(objective : TransportFacility, nbPassenger : Int) : Result = {
    sendPassenger(objective, nbPassenger) match {
      case Success() =>
        waitingPassengers -= objective
        stats.newEvent("Sent passengers to " + objective.town.name, nbPassenger)
        Success()

      case Failure(reason) =>
        val newWaitingPassengers = Integer.parseInt(reason)
        waitingPassengers += ((objective, newWaitingPassengers))
        stats.newEvent("Fail sending passengers to " + objective.town.name, newWaitingPassengers)
        Failure(reason)
    }
  }

  /**
    * Send @nbPassenger to @objective
    *
    * @param objective The transport facility objective
    * @param nbPassenger The number of passenger to send
    */
  private def sendPassenger(objective : TransportFacility, nbPassenger : Int) : Result = {
    if (vehicles.isEmpty)
      return Failure(nbPassenger.toString)

    getRoadTo(objective) match {
      case Some(road) =>
        if (road.nbVehicle == road.capacity)
          return Failure(nbPassenger.toString)

        val vehicleOpt = availableVehicle()
        if (vehicleOpt.isEmpty)
          return Failure(nbPassenger.toString)

        val vehicle = vehicleOpt.get

        loadPassenger(vehicle, objective, nbPassenger)

        putOnRoad(vehicle, road) match {
          case Success() => Success()

          case _ =>
            unload(vehicle)
            Failure(nbPassenger.toString)
        }

      case None =>
        Failure(nbPassenger.toString)
    }
  }

  /**
    * Try to send [cargoes] from this facility to [objective]
    * in order to fulfill [contract]
    *
    * @param objective The facility where to send cargoes
    * @param cargoes The cargoes to send
    * @param contract The contract related to this shipment
    * @return Success or failure depending if the cargoes have actually been sent
    */
  def trySendCargoes(objective : TransportFacility, cargoes : ListBuffer[Cargo], contract : Contract) : Result = {
    sendCargoes(objective, cargoes, contract) match {
      case Success() =>
        stats.newEvent("Cargoes sent to " + objective.town.name, cargoes.size)
        Success()

      case Failure(reason) =>
        stats.newEvent("Fail to send cargoes to " + objective.town.name
          + " " + reason, cargoes.size)
        Failure(reason)
    }
  }

  private def sendCargoes(objective : TransportFacility, cargoes : ListBuffer[Cargo], contract : Contract) : Result = {
    if (vehicles.isEmpty)
      return Failure("No available vehicle")

    getRoadTo(objective) match {
      case Some(road) =>
        if (road.nbVehicle == road.capacity)
          return Failure("Road is full")

        val vehicleOpt = availableVehicle(true)
        if (vehicleOpt.isEmpty)
          return Failure("No available resource transport vehicle")

        val vehicle = vehicleOpt.get

        loadCargoes(vehicle, objective, cargoes)
        vehicle.setContract(contract)

        putOnRoad(vehicle, road) match {
          case Success() =>
            Success()

          case failure =>
            unload(vehicle)
            vehicle.contractOpt = None
            failure
        }

      case None =>
        Failure("No road to transport facility")
    }
  }

  /**
    * @return One available vehicle
    */
  private def availableVehicle(forResourceTransport : Boolean = false) : Option[Vehicle] = {
    val vehicleOpt = vehicles.find(vehicle => {
      if (forResourceTransport)
        vehicle.canTransportResource && vehicle.destination.isEmpty

      else vehicle.destination.isEmpty
    })

    if (vehicleOpt.nonEmpty) vehicles -= vehicleOpt.get

    vehicleOpt
  }

  /**
    * @return The transport Facility neighbour of the one in parameter if it exists
    */
  def getRoadTo(transportFacility: TransportFacility) : Option[Road] = {
    roads.find(road => road.transportFacilityA == transportFacility
      || road.transportFacilityB == transportFacility)
  }

  /**
    * @param vehicle The vehicle to load
    * @param objective The objective of this vehicle
    * @param nbPassenger The number of passenger to load
    * @return The number of loaded passenger
    */
  def loadPassenger(vehicle: Vehicle, objective : TransportFacility, nbPassenger : Int) : Int = {
    val loadedPassenger = vehicle.loadPassenger(nbPassenger)

    val remainingPassenger = nbPassenger - loadedPassenger
    if (remainingPassenger > 0) waitingPassengers += ((objective, remainingPassenger))

    town.takePeople(loadedPassenger)

    vehicle.setObjective(objective)

    loadedPassenger
  }

  def loadCargoes(vehicle : Vehicle, objective : TransportFacility, cargoes : ListBuffer[Cargo]) : Unit = {
    vehicle.loadCargoes(cargoes)

    vehicle.setObjective(objective)
  }

  /**
    * @param vehicle The vehicle to unload
    */
  def unload(vehicle : Vehicle) : Unit = {
    vehicle.currentTransportFacility = Some(this)

    vehicle.destination match {
      case Some(transportFacility) =>
        if (transportFacility != this) {
          sendToDestination(vehicle)

          return
        } else {
          vehicle.destination = None
        }

      case None =>
    }

    vehicle.unsetObjective()

    town.receivePeople(vehicle.unloadPassenger())

    vehicle.contractOpt match {
      case Some(contract) =>
        company.fulfillContract(contract)

      case None =>
    }

    val cargoes = vehicle.unloadCargoes()
    town.receiveCargoes(cargoes)

    town.computeCurrentRequestAndOffer()

    vehicles += vehicle
  }

  /**
    * @param vehicle Send this vehicle to its destination if it have one
    */
  def sendToDestination(vehicle : Vehicle) : Result = {
    company.indicateNextObjective(vehicle)

    vehicle.goalTransportFacility match {
      case Some(goal) =>
        val road = getRoadTo(goal).get

        putOnRoad(vehicle, road) match {
          case Success() => Success()

          case failure =>
            waitingVehicles += vehicle
            failure
        }


      case None => Failure("Vehicle does not have goal")
    }
  }

  /**
    * @param vehicle The vehicle to put it on the [road]
    * @param road The road to put the vehicle
    */
  private def putOnRoad(vehicle : Vehicle, road : Road) : Result = {
    company.refillFuel(vehicle)

    if (vehicles.contains(vehicle)) vehicles -= vehicle
    if (waitingVehicles.contains(vehicle)) waitingVehicles -= vehicle

    vehicle.currentTransportFacility = None

    vehicle.enterRoad(road) match {
      case Success() =>
        makePassengerPay(vehicle.nbPassenger(), road.length)
        Success()

      case failure => failure
    }

  }

  /**
    * Make passengers pay for they tickets
    *
    * @param nbPassenger The number of passenger
    * @param distance The distance they passengers will travel
    */
  private def makePassengerPay(nbPassenger : Int, distance : Double) : Unit = {
    val earn = nbPassenger.toDouble * distance * company.ticketPricePerKm

    company.earn(earn)

    stats.newEvent("Money earn", earn.toInt)
  }

  /**
    * Give +2 capacity to this station
    */
  override def evolve() : Unit = {
    super.evolve()

    _capacity += evolutionPlan.level(level).head.toInt

    company.buy(evolvePrice)
  }

  /* For the GUI */

  val transportFacilityLabel = new Label("=== " + transportFacilityType.name + " ===")
  val capacityLabel = new Label()
  val vehiclesLabel = new Label()
  val waitingPassengerLabel = new Label()

  labels = List(transportFacilityLabel, capacityLabel, vehiclesLabel,
    waitingPassengerLabel)

  panel.children.addAll(transportFacilityLabel, capacityLabel, vehiclesLabel,
    waitingPassengerLabel, evolveButton, statsButton)

  styleLabels(14)

  override def propertyPane() : Node = {
    capacityLabel.text = "Capacity : " + capacity
    vehiclesLabel.text = "Vehicles : " + availableVehicles
    waitingPassengerLabel.text = "Waiting passenger : " + nbWaitingPassengers()

    panel
  }

}
