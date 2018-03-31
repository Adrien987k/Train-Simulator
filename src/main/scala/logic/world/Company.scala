package logic.world

import logic.exceptions.CannotBuildItemException
import logic.items.transport.facilities.{Airport, Station, TransportFacility}
import logic.items.transport.roads.{BasicLine, BasicRail, Road}
import logic.items.transport.vehicules.Vehicle
import logic.world.towns.Town
import interface.{GlobalInformationPanel, OneVehicleInformationPanel, WorldCanvas}
import logic.Updatable
import logic.items.ItemTypes._
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.collections.ObservableBuffer

abstract class Company(world : World) {

  var money = 2000000.0
  var ticketPricePerKm = 0.01

  val vehicles : ObservableBuffer[Vehicle] = ObservableBuffer.empty
  val transportFacilities : ListBuffer[TransportFacility] = ListBuffer.empty
  val roads :  ListBuffer[Road] = ListBuffer.empty

  private var lastStation : Option[Station] = None
  private var selectedVehicle : Option[Vehicle] = None

  def step() : Unit = {
    vehicles.foreach(_.step())
    roads.foreach(_.step())
  }

  def addVehicle(vehicle : Vehicle) : Unit = {
    vehicles += vehicle
  }

  def destroyVehicle(vehicle : Vehicle) : Unit = {
    if (vehicles.contains(vehicle))
      vehicles -= vehicle

    if (selectedVehicle.nonEmpty && selectedVehicle.get == vehicle)
      selectedVehicle = None

    OneVehicleInformationPanel.remove(vehicle)
    WorldCanvas.removeIfSelected(vehicle)
  }

  def addTransportFacility(transportFacility : TransportFacility) : Unit = {
    transportFacilities += transportFacility
  }

  def refillFuel(vehicle : Vehicle) : Unit = {
    money -= Shop.fuelPrice(vehicle.engine.engineType)

    vehicle.refillFuel()
  }

  /**
    * Try to add to this company an element of type [itemType] at the position [pos]
    *
    * @param itemType The type of element to add
    * @param pos The position where to add it
    */
  def tryPlace(itemType : ItemType, pos : Pos) : Unit = {
    GlobalInformationPanel.removeWarningMessage()

    val updatable = world.updatableAt(pos) match {
      case Some(upd) => upd
      case None => return
    }

    try {
      place(itemType, updatable)
    } catch {
      case e : CannotBuildItemException =>
      GlobalInformationPanel.displayWarningMessage(e.getMessage)
    }
  }

  private def place(itemType : ItemType, updatable : Updatable) : Unit = {
    var quantity = 0

    if (!canBuy(itemType)) throw new CannotBuildItemException("Not enough money")

    (itemType, updatable) match {
      case (tfType : TransportFacilityType, town : Town) =>
        town.buildTransportFacility(tfType)

      case (vehicleType : VehicleType, town : Town) =>
        town.buildVehicle(vehicleType)

      case (RAIL, town : Town) =>
        if (!town.hasStation) throw new CannotBuildItemException("This town does not have a station")

        lastStation match {
          case Some(station) =>
            if (station == town.station.get) return

            if (roadAlreadyExist(station, town.station.get))
              throw new CannotBuildItemException("This rail already exists")

            lastStation = None

            quantity = station.pos.dist(town.station.get.pos).toInt

            if (!canBuy(itemType, quantity)) throw new CannotBuildItemException("Not enough money")

            buildRoad(RAIL, station, town.station.get)

          case None =>
            lastStation = Some(town.station.get)
            return
        }
      case _ => return
    }
    buy(itemType, quantity)
  }

  def selectVehicle(vehicle : Vehicle): Unit = {
    selectedVehicle = Some(vehicle)
  }

  def setTrainDestination(pos : Pos): Unit = {
    selectedVehicle.foreach(train =>
      world.updatableAt(pos) match {
        case Some(town : Town) =>
          train.setDestination(town)
        case _ =>
      }
    )
  }

  /**
    * @return True if a road already exist between [transportFacilityA] and [transportFacilityB]
    */
  private def roadAlreadyExist(transportFacilityA : TransportFacility, transportFacilityB : TransportFacility) : Boolean = {
    roads.exists(road =>
      road.transportFacilityA == transportFacilityA && road.transportFacilityB == transportFacilityB
      ||
      road.transportFacilityB == transportFacilityA && road.transportFacilityA == transportFacilityB)
  }

  private def canBuy(itemType : ItemType, quantity : Int = 1) : Boolean = {
    val price = Shop.price(itemType, quantity)
    money - price >= 0
  }

  def buy(itemType: ItemType, quantity : Int = 1): Unit = {
    val price = Shop.price(itemType, quantity)
    if (money - price < 0)
      throw new CannotBuildItemException("Not enough money")
    else {
      money -= price
    }
  }

  /**
    * Build a new road of type [roadType] between [transportFacilityA] and [transportFacilityB]
    *
    * @param roadType The type of road
    * @param transportFacilityA The first facility to connect
    * @param transportFacilityB The second facility to connect
    */
  def buildRoad(roadType : RoadType, transportFacilityA : TransportFacility, transportFacilityB : TransportFacility) : Unit = {
    val road = roadType match {
      case RAIL =>
        new BasicRail(RAIL, this,
          transportFacilityA.asInstanceOf[Station],
          transportFacilityB.asInstanceOf[Station],
          10.0)

      case LINE =>
        new BasicLine(LINE, this,
          transportFacilityA.asInstanceOf[Airport],
          transportFacilityB.asInstanceOf[Airport])
    }
    roads += road

    transportFacilityA.addRoad(road)
    transportFacilityB.addRoad(road)
  }

  /**
    * @return The list of all airport of this company
    */
  def getAirports : ListBuffer[Airport] = {
    transportFacilities.filter(_.isInstanceOf[Airport]).asInstanceOf[ListBuffer[Airport]]
  }

  def indicateNextObjective(vehicle : Vehicle) : Unit = {
    if (vehicle.destination.isEmpty) return

    //TODO A* + set the next station to this vehicle
  }

}
