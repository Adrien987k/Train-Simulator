package logic.items.transport.facilities

import interface.GlobalInformationPanel
import logic.exceptions.{AlreadyMaxLevelException, CannotBuildItemException, CannotSendPassengerException, ImpossibleActionException}
import logic.economy.ResourcePack
import logic.items.Facility
import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.Road
import logic.items.transport.vehicules.VehicleTypes.VehicleType
import logic.items.transport.vehicules.{Vehicle, VehicleFactory}
import logic.world.{Company, Shop}
import logic.world.towns.Town

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

abstract class TransportFacility
(val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 private var _capacity : Int)
  extends Facility(transportFacilityType, company, town) {

  def capacity : Int = _capacity

  private var waitingPassengers : mutable.HashMap[TransportFacility, Int] = mutable.HashMap.empty

  private val vehicles : ListBuffer[Vehicle] = ListBuffer.empty
  private val roads : ListBuffer[Road] = ListBuffer.empty

  private val waitingVehicles : ListBuffer[Vehicle] = ListBuffer.empty

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
  protected def buildVehicle(vehicleType : VehicleType) : Unit = {
    if (isFull) throw new CannotBuildItemException("This facility is full")

    val vehicle = VehicleFactory.make(vehicleType, company, this)

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

  def trySendResources(objective : TransportFacility, packs : ListBuffer[ResourcePack]) : Unit = {
    try {
      sendResources(objective, packs)
    } catch {
      case e : CannotSendPassengerException =>
        //TODO Change to an other exception
    }
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
  private def sendPassenger(objective : TransportFacility, nbPassenger : Int) : Unit = {
    if (vehicles.isEmpty) {
      throw new CannotSendPassengerException(nbPassenger)
    }

    getRoadTo(objective) match {
      case Some(road) =>
        if (road.nbVehicle == road.DEFAULT_CAPACITY)
          throw new CannotSendPassengerException(nbPassenger)

        val vehicleOpt = availableVehicle()
        if (vehicleOpt.isEmpty)
          throw new CannotSendPassengerException(nbPassenger)

        val vehicle = vehicleOpt.get

        loadPassenger(vehicle, objective, nbPassenger)

        try {
          putOnRoad(vehicle, road)
        } catch {
          case ImpossibleActionException(_) =>
            unload(vehicle)
            throw new CannotSendPassengerException(nbPassenger)
        }

      case None =>
        throw new CannotSendPassengerException(nbPassenger)
    }
  }

  private def sendResources(objective : TransportFacility, packs : ListBuffer[ResourcePack]) : Unit = {
    if (vehicles.isEmpty)
      throw new ImpossibleActionException("Cannot send resources")

    getRoadTo(objective) match {
      case Some(road) =>
        if (road.nbVehicle == road.DEFAULT_CAPACITY)
          throw new ImpossibleActionException("")

        val vehicleOpt = availableVehicle(true)
        if (vehicleOpt.isEmpty)
          throw new ImpossibleActionException("No available resource transport vehicle")

        val vehicle = vehicleOpt.get

        loadResources(vehicle, objective, packs)

        try {
          putOnRoad(vehicle, road)
        } catch {
          case ImpossibleActionException(_) =>
            unload(vehicle)
            throw new ImpossibleActionException("Cannot pu on road")
        }

      case None =>
        throw new ImpossibleActionException("No road to transport facility")
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
    var loadedPassenger = vehicle.loadPassenger(nbPassenger)

    val remainingPassenger = nbPassenger - loadedPassenger
    if (remainingPassenger > 0) waitingPassengers += ((objective, remainingPassenger))

    town.population -= loadedPassenger

    vehicle.setObjective(objective)

    loadedPassenger
  }

  def loadResources(vehicle : Vehicle, objective : TransportFacility, packs : ListBuffer[ResourcePack]) : Unit = {
    vehicle.loadResources(packs)

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

    town.population += vehicle.unloadPassenger()
    town.warehouse.storeResourcePacks(vehicle.unloadResources())

    vehicles += vehicle
  }

  /**
    * @param vehicle Send this vehicle to its destination if it have one
    */
  def sendToDestination(vehicle : Vehicle) : Unit = {
    company.indicateNextObjective(vehicle)

    vehicle.goalTransportFacility match {
      case Some(goal) =>
        val road = getRoadTo(goal).get

        try {
          putOnRoad(vehicle, road)
        } catch {
          case ImpossibleActionException(_) =>
            waitingVehicles += vehicle
        }

      case None =>
    }
  }

  /**
    * @param vehicle The vehicle to put it on the [road]
    * @param road The road to put the vehicle
    */
  private def putOnRoad(vehicle : Vehicle, road : Road) : Unit = {
    company.refillFuel(vehicle)

    if (vehicles.contains(vehicle)) vehicles -= vehicle
    if (waitingVehicles.contains(vehicle)) waitingVehicles -= vehicle

    vehicle.currentTransportFacility = None

    vehicle.enterRoad(road)

    makePassengerPay(vehicle.nbPassenger(), road.length)
  }

  /**
    * Make passengers pay for they tickets
    *
    * @param nbPassenger The number of passenger
    * @param distance The distance they passengers will travel
    */
  private def makePassengerPay(nbPassenger : Int, distance : Double) : Unit = {
    company.money += nbPassenger * distance * company.ticketPricePerKm
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

  /* For the GUI */

  val panel = new VBox()

  val transportFacilityLabel = new Label("=== " + transportFacilityType.name + " ===")
  val capacityLabel = new Label()
  val vehiclesLabel = new Label()
  val waitingPassengerLabel = new Label()

  val evolveButton = new Button("Evolve : " + Shop.evolutionPrice(transportFacilityType, 1) + "$")
  evolveButton.font = Font.font(null, FontWeight.ExtraBold, 19)
  evolveButton.textFill = Color.Green

  evolveButton.onAction = _ => {
    try {
      evolve()
    } catch {
      case _ : AlreadyMaxLevelException =>
        if (!levelIsMax) {
          if (panel.children.contains(evolveButton))
            panel.children.remove(evolveButton)
        }

      case _ : CannotBuildItemException =>
        GlobalInformationPanel.displayWarningMessage("Not enough money")
    }

    evolveButton.text =
      "Evolve : " + Shop.evolutionPrice(transportFacilityType, level) + "$"

  }

  labels = List(transportFacilityLabel, capacityLabel, vehiclesLabel, waitingPassengerLabel)

  panel.children = labels ++ List(evolveButton)

  styleLabels(14)

  override def propertyPane() : Node = {
    capacityLabel.text = "Capacity : " + capacity
    vehiclesLabel.text = "Vehicles : " + availableVehicles
    waitingPassengerLabel.text = "Waiting passenger : " + nbWaitingPassengers()

    panel
  }

}
