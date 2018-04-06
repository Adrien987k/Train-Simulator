package logic.world

import logic.exceptions.CannotBuildItemException
import logic.items.transport.facilities._
import logic.items.transport.roads.{Road, RoadFactory}
import logic.items.transport.vehicules.Vehicle
import logic.world.towns.Town
import interface.{GlobalInformationPanel, OneVehicleInformationPanel, WorldCanvas}
import logic.Updatable
import logic.items.ItemTypes
import logic.items.ItemTypes._
import utils.Pos

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.collections.ObservableBuffer

abstract class Company(world : World) {

  var money = 2000000.0
  var ticketPricePerKm = 0.01

  val vehicles : ObservableBuffer[Vehicle] = ObservableBuffer.empty
  val transportFacilities : ListBuffer[TransportFacility] = ListBuffer.empty
  val roads :  ListBuffer[Road] = ListBuffer.empty

  private var lastTransportFacilityOpt : Option[TransportFacility] = None
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
    if (!canBuy(itemType)) throw new CannotBuildItemException("Not enough money")

    (itemType, updatable) match {
      case (tfType : TransportFacilityType, town : Town) =>
        town.buildTransportFacility(tfType)
        buy(itemType)

      case (vehicleType : VehicleType, town : Town) =>
        town.buildVehicle(vehicleType)
        buy(itemType)

      case (roadType : RoadType, town : Town) =>
        tryBuildRoad(roadType, town)

      case _ =>
    }

  }

  private def tryBuildRoad(roadType : RoadType, town : Town) : Unit = {
    var quantity = 0

    val transportFacilityType = ItemTypes.transportFacilityTypeFromRoadType(roadType)

    if (!town.hasTransportFacilityOfType(transportFacilityType))
      throw new CannotBuildItemException("This town does not have " + transportFacilityType.name)

    val currentTransportFacility = town.transportFacilityOfType(transportFacilityType).get

    lastTransportFacilityOpt match {
      case Some(lastTransportFacility) =>

        if (lastTransportFacility == currentTransportFacility) return

        if (roadAlreadyExist(lastTransportFacility, currentTransportFacility))
          throw new CannotBuildItemException("This road already exists")

        lastTransportFacilityOpt = None

        quantity = lastTransportFacility.pos.dist(currentTransportFacility.pos).toInt

        if (!canBuy(roadType, quantity)) throw new CannotBuildItemException("Not enough money")

        buildRoad(roadType, lastTransportFacility, currentTransportFacility)

        buy(roadType, quantity)

      case None =>
        lastTransportFacilityOpt = Some(currentTransportFacility)
    }
  }

  def selectVehicle(vehicle : Vehicle) : Unit = {
    selectedVehicle = Some(vehicle)
  }

  /**
    * Try to set a destination to the selected vehicle
    * The destination is town that could be at position [pos]
    * If no town is found at position [pos] does not do anything
    *
    * @param pos The position where to search the destination town
    */
  def setVehicleDestination(pos : Pos) : Unit = {
    selectedVehicle.foreach(vehicle =>
      world.updatableAt(pos) match {
        case Some(town : Town) =>
          vehicle.setDestination(town)

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

  def buy(itemType : ItemType, quantity : Int = 1) : Unit = {
    val price = Shop.price(itemType, quantity)
    if (money - price < 0)
      throw new CannotBuildItemException("Not enough money")

    money -= price
  }

  def canBuyEvolution(itemType : ItemType, level : Int) : Boolean = {
    val price = Shop.evolutionPrice(itemType, level)
    money - price >= 0
  }

  def buyEvolution(itemType : ItemType, level : Int) : Unit = {
    val price = Shop.evolutionPrice(itemType, level)
    if (money - price < 0)
      throw new CannotBuildItemException("Not enough money to buy " + itemType.name + " evolution")

    money -= price
  }

  def CanBuyInfrastructure(itemType : ItemType, level : Int) : Boolean = {
    val price = Shop.evolutionPrice(itemType, level)
    money - price >= 0
  }

  /**
    * Build a new road of type [roadType] between [transportFacilityA] and [transportFacilityB]
    *
    * @param roadType The type of road
    * @param transportFacilityA The first facility to connect
    * @param transportFacilityB The second facility to connect
    */
  def buildRoad(roadType : RoadType, transportFacilityA : TransportFacility, transportFacilityB : TransportFacility) : Unit = {
    val road = RoadFactory.makeRoad(roadType, this, transportFacilityA, transportFacilityB)

    roads += road

    transportFacilityA.addRoad(road)
    transportFacilityB.addRoad(road)
  }

  /**
    * @return The list of all airport of this company
    */
  def getAirports : ListBuffer[Airport] =
    transportFacilities.filter(_.isInstanceOf[Airport]).asInstanceOf[ListBuffer[Airport]]

  /**
    * @return The list of all stations of this company
    */
  def getStations : ListBuffer[Station] =
    transportFacilities.filter(_.isInstanceOf[Station]).asInstanceOf[ListBuffer[Station]]

  /**
    * @return The list of all harbors of this company
    */
  def getHarbors : ListBuffer[Harbor] =
    transportFacilities.filter(_.isInstanceOf[Harbor]).asInstanceOf[ListBuffer[Harbor]]

  /**
    * @return The list of all stations of this company
    */
  def getGasStations : ListBuffer[GasStation] =
    transportFacilities.filter(_.isInstanceOf[GasStation]).asInstanceOf[ListBuffer[GasStation]]

  /**
    * @param transportFacilityType The transport facility type
    * @return The list of all transport facilities of type [transportFacilityType]
    *         of this company
    */
  def getTransportFacilitiesOfType(transportFacilityType : TransportFacilityType) : ListBuffer[TransportFacility] = {
    val result = transportFacilityType match {
      case AIRPORT => getAirports
      case STATION => getStations
      case HARBOR => getHarbors
      case GAS_STATION => getGasStations
    }

    result.asInstanceOf[ListBuffer[TransportFacility]]
  }


  /**
    * Found the shortest path between [vehicle] current transport facility
    * and destination and set the appropriate goal transport facility
    * (Goal transport facility is the next facility to reach in one step)
    *
    * Don't do anything if [vehicle] does not have a destination or a current station
    *
    * @param vehicle The vehicle to set goal transport facility
    */
  def indicateNextObjective(vehicle : Vehicle) : Unit = {
    if (vehicle.destination.isEmpty) return

    val destination = vehicle.destination.get

    val startTransportFacility = vehicle.currentTransportFacility match {
      case Some(transportFacility) => transportFacility

      case None => return
    }

    val transportFacilityType = ItemTypes.transportFacilityFromVehicle(vehicle.vehicleType)

    val transportFacilities = getTransportFacilitiesOfType(transportFacilityType)

    val nodes = new mutable.HashMap[TransportFacility, Node]
    transportFacilities.foreach(tf =>
      nodes += ((tf, new Node(tf))))

    val queue : ListBuffer[Node] = new ListBuffer[Node]()

    val startNode = nodes(startTransportFacility)

    nodes.foreach(c => {
      queue += c._2
    })

    startNode.dist = 0.0

    while (queue.nonEmpty) {
      val currentNode = queue.foldLeft(queue.head)((acc, node) => if (node.dist < acc.dist) node else acc)
      queue -= currentNode

      val neighbours = currentNode.transportFacility.neighbours()
      neighbours.foreach(tf => {
        val neighbourNode = nodes(tf)

        val roadOpt = tf.getRoadTo(currentNode.transportFacility)
        val road = roadOpt match {
          case Some(r) => r

          case None =>
            vehicle.goalTransportFacility = None

            return
        }

        if (neighbourNode.dist > currentNode.dist + road.length) {
          neighbourNode.dist = currentNode.dist + road.length
          neighbourNode.prec = currentNode
        }

      })
    }

    val path : ListBuffer[Node] = ListBuffer.empty

    var destinationNode = nodes(destination)

    while (destinationNode != null) {
      path += destinationNode
      destinationNode = destinationNode.prec
    }

    if (path.lengthCompare(1) > 0) {
      path.remove(path.size - 1)
      vehicle.goalTransportFacility = None
      vehicle.setObjective(path.last.transportFacility)
    }
  }

  private class Node(val transportFacility : TransportFacility) {
    var dist : Double = Double.MaxValue
    var visited : Boolean = false

    var prec : Node = _
  }

}
