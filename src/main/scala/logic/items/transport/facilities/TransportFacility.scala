package logic.items.transport.facilities

import logic.exceptions.{CannotBuildItemException, CannotSendPassengerException, ImpossibleActionException}
import logic.PointUpdatable
import logic.items.Item
import logic.items.ItemTypes.{TransportFacilityType, VehicleType}
import logic.items.transport.roads.Road
import logic.items.transport.vehicules.{Vehicle, VehicleFactory}
import logic.world.Company
import logic.world.towns.Town

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

abstract class TransportFacility
(val transportFacilityType: TransportFacilityType,
 override val company : Company,
 val town : Town,
 private var _capacity : Int)
  extends Item(transportFacilityType, company) with PointUpdatable {

  updateRate(1)
  pos = town.pos

  def capacity : Int = _capacity

  private var waitingPassengers : mutable.HashMap[TransportFacility, Int] = mutable.HashMap.empty

  private val vehicles : ListBuffer[Vehicle] = ListBuffer.empty
  private val roads : ListBuffer[Road] = ListBuffer.empty

  /**
    * @return True if vehicle capacity is reached
    */
  def isFull : Boolean = vehicles.lengthCompare(capacity) == 0

  def nbNeighbours() : Int = roads.size

  /**
    * @return The number of available vehicles
    */
  def availableVehicles : Int = vehicles.size

  def addRoad(road : Road) : Unit = {
    roads += road
  }

  override def step() : Boolean = {
    if(!super.step()) return false

    for ((transportFacility, nbPassenger) <- waitingPassengers) {
      trySendPassenger(transportFacility, nbPassenger)
    }

    true
  }

  /**
    * Build a new vehicle of type [vehicleType]
    *
    * @param vehicleType the type of vehicle to build
    */
  protected def buildVehicle(vehicleType : VehicleType) : Unit = {
    if (isFull) throw new CannotBuildItemException("This facility is full")

    val vehicle = VehicleFactory.makeVehicle(vehicleType, company, this)

    company.addVehicle(vehicle)
    vehicles += vehicle
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
  def trySendPassenger(objective : TransportFacility, nbPassenger : Int) : Unit = {
    try {
      sendPassenger(objective, nbPassenger)
    } catch {
    case e : CannotSendPassengerException =>
      val newWaitingPassengers = Integer.parseInt(e.getMessage)
      waitingPassengers += ((objective, newWaitingPassengers))
      return
  }

  waitingPassengers -= objective
  }

  /**
    * Send @nbPassenger to @objective
    *
    * @param objective The transport facility objective
    * @param nbPassenger The number of passenger to send
    */
  def sendPassenger(objective : TransportFacility, nbPassenger : Int) : Unit = {
    if (vehicles.isEmpty) {
      throw new CannotSendPassengerException(nbPassenger)
    }

    getRoadTo(objective) match {
      case Some(road) =>
        if (road.nbVehicle == road.DEFAULT_CAPACITY) {
          throw new CannotSendPassengerException(nbPassenger)
        }

        val vehicleOpt = availableVehicle()
        if (vehicleOpt.isEmpty)
          throw new CannotSendPassengerException(nbPassenger)

        val vehicle = vehicleOpt.get

        val loadedPassenger = load(vehicle, objective, nbPassenger)
        try {
          vehicle.putOnRoad(road)
        } catch {
          case ImpossibleActionException(_) =>
            unload(vehicle)
            throw new CannotSendPassengerException(nbPassenger)
        }
        company.money += loadedPassenger * road.length * company.ticketPricePerKm

      case None =>
        throw new CannotSendPassengerException(nbPassenger)
    }
  }

  /**
    * @return One available vehicle
    */
  private def availableVehicle() : Option[Vehicle] = {
    val vehicleOpt = vehicles.find(_.destination.isEmpty)

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
  def load(vehicle: Vehicle, objective : TransportFacility, nbPassenger : Int) : Int = {
    var loadedPassenger = vehicle.loadPassenger(nbPassenger)

    val remainingPassenger = nbPassenger - loadedPassenger
    if (remainingPassenger > 0) waitingPassengers += ((objective, remainingPassenger))

    town.population -= loadedPassenger

    vehicle.setObjective(objective)

    loadedPassenger
  }

  /**
    * @param vehicle The vehicle to unload
    */
  def unload(vehicle: Vehicle) : Unit = {
    vehicle.unsetObjective()

    town.population += vehicle.unloadPassenger()

    vehicles += vehicle
  }

  /**
    * Give +2 capacity to this station
    */
  override def evolve() : Unit = {
    if (!company.canBuyEvolution(transportFacilityType, level + 1))
      throw new CannotBuildItemException("Not enough money to evolve" + transportFacilityType.name)

    _capacity += level
    level += 1

    company.buyEvolution(transportFacilityType, level + 1)
  }

}
