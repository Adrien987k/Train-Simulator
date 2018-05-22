package logic.world

import logic.items.transport.facilities._
import logic.items.transport.roads.{Road, RoadFactory}
import logic.items.transport.vehicules.Vehicle
import logic.world.towns.Town
import interface.{AllVehiclesInformationPanel, GlobalInformationPanel, OneVehicleInformationPanel, WorldCanvas}
import logic.Updatable
import logic.economy.{Cargo, ResourcePack}
import logic.economy.Resources.{BOXED, DRY_BULK, LIQUID}
import logic.items.ItemTypes
import logic.items.ItemTypes._
import logic.items.production.FactoryTypes.FactoryType
import logic.items.transport.facilities.TransportFacilityTypes._
import logic.items.transport.roads.RoadTypes.{LINE, RoadType, WATERWAY}
import logic.items.transport.vehicules.VehicleTypes.VehicleType
import statistics.Statistics
import utils.{Failure, Pos, Result, Success}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Company(world : World) {

  var _money = 2000000.0
  private val _ticketPricePerKm = 0.01

  val vehicles : ListBuffer[Vehicle] = ListBuffer.empty
  val transportFacilities : ListBuffer[TransportFacility] = ListBuffer.empty
  val roads :  ListBuffer[Road] = ListBuffer.empty

  private var lastTransportFacilityOpt : Option[TransportFacility] = None
  private var selectedVehicle : Option[Vehicle] = None

  private val contracts : ListBuffer[Contract] = ListBuffer.empty

  private val stats = new Statistics("Company")
  private val contractStats = new Statistics("Contracts")
  contractStats.deactivateAverages()

  def money : Double = _money
  def ticketPricePerKm : Double = _ticketPricePerKm

  def step() : Unit = {
    vehicles.foreach(_.step())
    roads.foreach(_.step())

    answerContract()
  }

  def addVehicle(vehicle : Vehicle) : Unit = {
    vehicles += vehicle
    AllVehiclesInformationPanel.addVehicleButton(vehicle)
  }

  def destroyVehicle(vehicle : Vehicle) : Unit = {
    if (vehicles.contains(vehicle)) {
      vehicles -= vehicle
      AllVehiclesInformationPanel.removeVehicleButton(vehicle)
    }

    if (selectedVehicle.nonEmpty && selectedVehicle.get == vehicle)
      selectedVehicle = None

    OneVehicleInformationPanel.remove(vehicle)
    WorldCanvas.removeIfSelected(vehicle)
  }

  def addTransportFacility(transportFacility : TransportFacility) : Unit = {
    transportFacilities += transportFacility
  }

  /**
    * @param vehicle refill fuel of that vehicle
    */
  def refillFuel(vehicle : Vehicle) : Unit = {
    _money -= vehicle.totalWeight / 1000

    vehicle.refillFuel()

    stats.newEvent("refill fuel of " + vehicle.vehicleType.name)
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

    place(itemType, updatable) match {
      case Success() =>
        stats.newEvent(itemType.name + " built")

      case Failure(reason) =>
        GlobalInformationPanel.displayWarningMessage(reason)
    }
  }

  private def place(itemType : ItemType, updatable : Updatable) : Result = {
    if (!canBuy(itemType.price))
      return Failure("Not enough money")

    (itemType, updatable) match {
      case (tfType : TransportFacilityType, town : Town) =>
        town.buildTransportFacility(tfType) match {
          case Success() =>
            return buy(itemType.price)

          case failure => return failure
        }

      case (vehicleType : VehicleType, town : Town) =>
        town.buildVehicle(vehicleType) match {
          case Success() =>
            return buy(itemType.price)

          case failure => return failure
        }

      case (roadType : RoadType, town : Town) =>
        return tryBuildRoad(roadType, town)

      case (factoryType : FactoryType, town : Town) =>
        return town.buildFactory(factoryType)

      case _ =>
    }

    Success()
  }

  private def tryBuildRoad(roadType : RoadType, town : Town) : Result = {
    var quantity = 0

    val transportFacilityType = ItemTypes.transportFacilityTypeFromRoadType(roadType)

    if (!town.hasTransportFacilityOfType(transportFacilityType))
      return Failure("This town does not have " + transportFacilityType.name)

    val currentTransportFacility = town.transportFacilityOfType(transportFacilityType).get

    lastTransportFacilityOpt match {
      case Some(lastTransportFacility) =>
        quantity = lastTransportFacility.pos.dist(currentTransportFacility.pos).toInt

        if (!canBuy(roadType.price, quantity)) return Failure("Not enough money")

        buildRoad(roadType, lastTransportFacility, currentTransportFacility) match {
          case Success() =>

          case failure => return failure
        }

        buy(roadType.price, quantity)

        lastTransportFacilityOpt = None

        Success()

      case None =>
        lastTransportFacilityOpt = Some(currentTransportFacility)

        Success()
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
          stats.newEvent("Set destination of " + vehicle.vehicleType.name + " to " + town.name)

        case _ =>
      }
    )
  }

  /**
    * @return True if a road already exist between [transportFacilityA] and [transportFacilityB]
    */
  def roadAlreadyExists(townA : Town, townB : Town) : Boolean = {
    roads.exists(road => {
      road.roadType match {
        case LINE => return false
        case _ =>
          (road.transportFacilityA.town == townA && road.transportFacilityB.town == townB) ||
          (road.transportFacilityB.town == townA && road.transportFacilityA.town == townB)
      }
    })
  }

  def canBuy(amount : Double, quantity : Int = 1) : Boolean = {
    _money - (amount * quantity) >= 0
  }

  /**
    * @param amount The amount of money for one element
    * @param quantity The number of elements
    * @return Success or failure
    */
  def buy(amount : Double, quantity : Int = 1) : Result = {
    val totalAmount = amount * quantity

    if (_money - totalAmount < 0)
      Failure("Not enough money")

    _money -= totalAmount

    stats.newEvent("purchase", totalAmount.toInt)

    Success()
  }

  /**
    * @param amount The amount of money to earn
    */
  def earn(amount : Double) : Unit = {
    _money += amount

    if (amount > 0)
      stats.newEvent("Earn", amount.toInt)
  }

  /**
    * Build a new road of type [roadType] between [transportFacilityA] and [transportFacilityB]
    *
    * @param roadType The type of road
    * @param transportFacilityA The first facility to connect
    * @param transportFacilityB The second facility to connect
    */
  def buildRoad(roadType : RoadType, transportFacilityA : TransportFacility, transportFacilityB : TransportFacility) : Result = {
    if (transportFacilityA == transportFacilityB) return Failure("Cannot build road from to the same town")

    if (roadType != WATERWAY && world.existNaturalWaterWay(transportFacilityA.town, transportFacilityB.town))
      return Failure("Cannot build road on waterway")

    if (roadAlreadyExists(transportFacilityA.town, transportFacilityB.town))
      return Failure ("Road already exists there")

    val road = RoadFactory.make(roadType, this, transportFacilityA, transportFacilityB)

    roads += road

    transportFacilityA.connectRoad(road)
    transportFacilityB.connectRoad(road)

    Success()
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
    * Create a new contract between a town and the company
    * for transport resources
    *
    * @param from The town where to start
    * @param to The town where to deliver resources
    * @param packs The resources to deliver
    */
  def createContract(from : Town, to : Town, packs : ListBuffer[ResourcePack], reward : Double) : Unit = {

    contracts.find(contractTaken => {
      from == contractTaken.from &&
      to == contractTaken.to &&
        contractTaken.status != FULFILLED
    }) match {
      case Some(_) => return
      case None =>
    }

    val contract = Contract(from, to, packs, reward, CREATED)

    contracts += contract

    stats.newEvent("Contract created from " + from.name + " to " + to.name)
    contractStats.newEvent("Contract", contract)
  }

  /**
    * Try to send vehicles in order to fulfill as more as possible contracts
    */
  def answerContract() : Unit = {
    contracts.foreach(contract => {
      val shouldAnswer = contract.status match {
        case VEHICLE_SENT => false
        case FULFILLED => false
        case _ => true
      }

      if (shouldAnswer) {
        val cargoDryBulk = new Cargo(DRY_BULK)
        val cargoLiquid = new Cargo(LIQUID)
        val cargoBoxed = new Cargo(BOXED)

        contract.packs.foldLeft(None)((None, pack) => {
          pack.resource.resourceType match {
            case DRY_BULK => cargoDryBulk.store(ListBuffer(pack))
            case LIQUID => cargoLiquid.store(ListBuffer(pack))
            case BOXED => cargoBoxed.store(ListBuffer(pack))
          }

          None
        })

        val cargoes = ListBuffer(cargoDryBulk, cargoLiquid, cargoBoxed)

        contract.from.transportFacilities().foreach(tfOpt => {
          tfOpt.foreach(tf => {

            if (contract.from.neighboursOf(tfOpt).contains(contract.to))
              tf.trySendCargoes(contract.to.transportFacilityOfType(tf.transportFacilityType).get,
                cargoes, contract) match {
                case Success() =>
                  stats.newEvent("Vehicle sent for contract from " +
                    contract.from.name + " to " + contract.to.name)
                  contract.status = VEHICLE_SENT

                case Failure(reason) =>
                  println("FAILURE " + reason)

              }
          })
        })
      }
    })
  }

  /**
    * Check if this contract exists and fulfilled it
    *
    * @param contract The contract to check
    */
  def fulfillContract(contract : Contract) : Unit = {
    contracts.foreach(contractTaken => {
      if (contractTaken == contract) {
        contract.status = FULFILLED

        earn(contract.reward)
      }
    })
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

  def showStatistics() : Unit = stats.show()

  def showContractStatistics() : Unit = contractStats.show()

}
