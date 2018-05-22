package logic.world.towns

import game.Game
import logic.economy.Resources.Resource
import logic.{PointUpdatable, UpdateRate}
import logic.economy._
import logic.items.production.FactoryTypes.FactoryType
import logic.items.production.{Factory, FactoryFactory}
import logic.items.transport.facilities.TransportFacilityTypes._
import logic.items.transport.facilities._
import logic.items.transport.vehicules.VehicleTypes._
import logic.world.World
import statistics.Statistics
import utils.{Failure, Pos, Result, Success}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.scene.text.{Font, FontWeight}

class Town
(val world : World,
 _pos : Pos,
 private var _name : String)
  extends PointUpdatable {

  updateRate(UpdateRate.TOWN_UPDATE)
  pos = _pos

  private val rand = new Random()

  private val SEARCH_RADIUS = 1000

  val MAX_POPULATION = 1000000
  val MIN_POPULATION = 10
  val DEFAULT_PROPORTION_TRAVELER = 0.1
  val INIT_MAX_NB_FACTORY = 5

  var station : Option[Station] = None
  var airport : Option[Airport] = None
  var harbor : Option[Harbor] = None
  var gasStation : Option[GasStation] = None

  private var _population : Int = MIN_POPULATION +
      new Random().nextInt(MAX_POPULATION + 1 - MIN_POPULATION)

  private val proportionTraveler : Double = DEFAULT_PROPORTION_TRAVELER

  private val factories : ListBuffer[Factory] = ListBuffer.empty

  private val maxNbFactory : Int = INIT_MAX_NB_FACTORY

  private val offer : ResourceCollection = new ResourceCollection
  private val requests : ResourceMap = new ResourceMap
  private val consumption : ResourceMap = Consumption.initialConsumption()
  val warehouse : ResourceCollection = new ResourceCollection

  private var requestFromOtherCities = new ListBuffer[Request]

  private val stats = new Statistics("city")

  def name : String = _name
  def population : Int = _population

  def hasStation : Boolean = station.nonEmpty
  def hasAirport : Boolean = airport.nonEmpty
  def hasHarbor : Boolean = harbor.nonEmpty
  def hasGasStation : Boolean = gasStation.nonEmpty

  override def step() : Boolean = {
    if(!super.step()) return false

    val traveler = (proportionTraveler * _population / 100).toInt
    if (traveler != 0)
      sendPeopleToNeighbours(traveler)

    station.foreach(_.step())
    airport.foreach(_.step())
    harbor.foreach(_.step())
    gasStation.foreach(_.step())

    produce()

    consume()

    computeCurrentRequestAndOffer()

    searchResource()

    answerRequests()

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
  def buildTransportFacility(tfType : TransportFacilityType) : Result = {
    def buildInternal(tfOpt : Option[TransportFacility], itemNameForError : String) : (Option[TransportFacility], Result) = {
      tfOpt match {
        case Some(_) =>
          (None, Failure("This town already have " + itemNameForError))

        case None =>
          val tf = TransportFacilityFactory.make(tfType, world.company, this)
          world.company.addTransportFacility(tf)
          stats.newEvent(tfType.name + " built")
          (Some(tf), Success())
      }
    }

    buildInternal(transportFacilityOfType(tfType), tfType.name) match {
      case (Some(tf), result : Result) =>
          tfType match {
            case STATION => station = Some(tf.asInstanceOf[Station])
            case AIRPORT => airport = Some(tf.asInstanceOf[Airport])
            case HARBOR => harbor = Some(tf.asInstanceOf[Harbor])
            case GAS_STATION => gasStation = Some(tf.asInstanceOf[GasStation])
          }
        result

      case(None, failure) => failure
    }
  }

  /**
    * Build a factory in that town
    *
    * @param factoryType The type of factory to build
    * @return Success or failure
    */
  def buildFactory(factoryType : FactoryType) : Result = {
    if (!world.company.canBuy(factoryType.price))
      return Failure("Not enough money")

    if (factories.lengthCompare(maxNbFactory) == 0)
      return Failure("Max number of factory reached")

    factories += FactoryFactory.make(factoryType, world.company, this)

    stats.newEvent(factoryType.name + " built")

    world.company.buy(factoryType.price)

    Success()
  }

  /**
    * Build a new vehicle of type [vehicleType] and add it to the appropriate
    * transport facility of this town if it exists. If not, display a warning
    * message to the player
    *
    * @param vehicleType The type of vehicle to build
    */
  def buildVehicle(vehicleType : VehicleType) : Result = {
    var result : Result = Success()

    vehicleType match {
      case _ : TrainType =>
        if (!hasStation) return Failure("This town does not have a Station")
        result = station.get.buildTrain(vehicleType.asInstanceOf[TrainType])

      case _ : PlaneType =>
        if (!hasAirport) return Failure("This town does not have an airport")
        result = airport.get.buildPlane(vehicleType.asInstanceOf[PlaneType])

      case _ : ShipType =>
        if (!hasHarbor) return Failure("This town does not have an harbor")
        result = harbor.get.buildShip(vehicleType.asInstanceOf[ShipType])

      case _ : TruckType =>
        if (!hasGasStation) return Failure("This town does not have a gas station")
        result = gasStation.get.buildTruck(vehicleType.asInstanceOf[TruckType])
    }

    result match {
      case Success() =>
        stats.newEvent(vehicleType.name + " built")
        Success()

      case failure => failure
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

  def takePeople(nbPassenger : Int) : Unit = {
    _population -= nbPassenger

    stats.newEvent("People leaving the city", nbPassenger)
  }

  def receivePeople(nbPassenger : Int) : Unit = {
    _population += nbPassenger

    stats.newEvent("People arriving to the city", nbPassenger)
  }

  /**
    * Receive new cargoes and manage them
    *
    * @param cargoes The cargoes to manage
    */
  def receiveCargoes(cargoes : ListBuffer[Cargo]) : Unit = {
    val packs = cargoes.foldLeft(ListBuffer.empty[ResourcePack])((packs, cargo) => {
      packs ++= cargo.takeAll()
    })

    //TODO do somthing with cargoes
    warehouse.storeResourcePacks(packs)

    stats.newEvent("Received packs", packs.size)
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

  def neighboursOf(transportFacilityOpt : Option[TransportFacility]) : ListBuffer[Town] = {
    transportFacilityOpt match {
      case Some(tf) => tf.neighbours().map(tf => tf.town)
      case None => ListBuffer()
    }
  }

  def neighbours() : ListBuffer[Town] = {
    neighboursOf(station) ++
    neighboursOf(airport) ++
    neighboursOf(harbor) ++
    neighboursOf(gasStation)
  }

  def transportFacilities() : List[Option[TransportFacility]] = {
    List(station, airport, harbor, gasStation)
  }

  def produce() : Unit = {
    factories.foreach(_.step())
  }

  /**
    * Consume resources from consumption
    * If not enough resource is available,
    * create requests for that resource
    */
  def consume() : Unit = {
    consumption.resources.foreach(resourceAndQuantity => {
      val (resource, quantity) = resourceAndQuantity

      var (_, missingQuantity) = warehouse.take(resource, quantity)

      if (missingQuantity > 0)
        missingQuantity = offer.take(resource, missingQuantity)._2

      if (missingQuantity > 0)
        stats.newEvent("City missing resource", new ResourcePack(resource, quantity))

      if (missingQuantity > 0
        && requests.quantityOf(resource) * 2 <= consumption.quantityOf(resource)) {
        requests.addSome(resource, missingQuantity)
      } else if (missingQuantity == 0) {
        requests.removeAll(resource)
      }
    })
  }

  def computeCurrentRequestAndOffer() : Unit = {
    warehouse.resourceMap().resources.foreach(resourceAndQuantity => {
      val (resource, quantity) = resourceAndQuantity

      val quantityConsumption = consumption.quantityOf(resource)

      if (quantity > 3 * quantityConsumption) {
        offer.storeResourcePack(warehouse.take(resource, quantity / 3)._1)
      }
    })

    offer.resourceMap().resources.foreach(resourceAndQuantity => {
      val (resource, _) = resourceAndQuantity

      if (requests.quantityOf(resource) > 0) {
        requests.removeAll(resource)
      }
    })
  }

  /**
    * Search the nearby cities for requested resources
    */
  def searchResource() : Unit = {
    Game.world.towns.foreach(town => {
      if (town.pos.dist(pos) < SEARCH_RADIUS) {
        requests.resources.foreach(request => {
          searchResourceIn(town, request)
        })
      }
    })
  }

  /**
    * Search for requested resources in [town]
    *
    * @param town The town where to search
    * @param request The request to send
    */
  private def searchResourceIn(town : Town, request : (Resource, Int)) : Unit = {
    if (town.offer.quantityOf(request._1) > 0) {
      println("request " + request._1.name + " " + request._2)

      town.takeRequestFromOtherTown(Request(this, request._1, request._2))
    }
  }

  /**
    * Take a request from another city
    *
    * @param request The request to take
    */
  def takeRequestFromOtherTown(request : Request) : Unit = {
    requestFromOtherCities += request

    stats.newEvent("Request received", request)
  }

  /**
    * Try answer as much as possible requests from other cities
    */
  def answerRequests() : Unit = {
    val packsTo : mutable.HashMap[Town, ListBuffer[ResourcePack]] = mutable.HashMap.empty

    requestFromOtherCities.foreach(request => {
      val canOfferQuantity = offer.quantityOf(request.resource)

      if (canOfferQuantity > 0) {
        val (toSendPack, _) = offer.take(request.resource, request.quantity)

        if (packsTo.contains(request.town)) {
          val list = packsTo(request.town)

          list += toSendPack

          packsTo.update(request.town, list)
        } else {
          packsTo += ((request.town, ListBuffer(toSendPack)))
        }
      }
    })

    packsTo.foreach(townAndPacks => {
      val (town, packs) = townAndPacks

      requestFromOtherCities = requestFromOtherCities.filter(request => {
        request.town != town
      })

      world.company.createContract(this, town, packs, pos.dist(town.pos))
    })
  }

  private val mainPanel = new BorderPane

  private val townPanel = new VBox()

  private val nameLabel = new Label("=== " + name + " ===")
  private val populationLabel = new Label()
  private val propTravelerLabel = new Label()
  private val posLabel = new Label()

  private val statsButton = new Button("City Statistics")

  private val facilitiesVBox = new VBox
  private val factoriesVBox = new VBox

  statsButton.font = Font.font(null, FontWeight.Bold, 18)
  statsButton.onAction = _ => {
    stats.show()
  }

  labels = List(nameLabel,
    populationLabel,
    propTravelerLabel,
    posLabel,
    new Label("\n"))

  styleLabels()

  townPanel.children = labels ++ List(statsButton)

  private var consumptionNode : Node = consumption.propertyPane()
  private var warehouseNode : Node = warehouse.propertyPane()
  private var offerNode : Node = offer.propertyPane()
  private var requestsNode : Node = requests.propertyPane()

  private val facilitiesNodes =
    List(
      new Label("=== Consumption ===\n"),
      consumptionNode,
      new Label("=== Warehouse ===\n"),
      warehouseNode,
      new Label("=== Offer ===\n"),
      offerNode,
      new Label("=== Requests ===\n"),
      requestsNode)

  facilitiesNodes.foreach(facilitiesVBox.children.add(_))

  mainPanel.top = townPanel
  mainPanel.center = facilitiesVBox
  mainPanel.bottom = factoriesVBox

  override def propertyPane() : Node = {
    populationLabel.text  = "Population : " + _population
    propTravelerLabel.text = "Proportion of traveler : " + proportionTraveler
    posLabel.text = "position : " + pos

    consumptionNode = consumption.propertyPane()
    warehouseNode = warehouse.propertyPane()
    offerNode = offer.propertyPane()
    requestsNode = requests.propertyPane()

    if (hasStation && !facilitiesVBox.children.contains(station.get.propertyPane()))
    facilitiesVBox.children.add(station.get.propertyPane())

    if (hasAirport && !facilitiesVBox.children.contains(airport.get.propertyPane()))
    facilitiesVBox.children.add(airport.get.propertyPane())

    if (hasHarbor && !facilitiesVBox.children.contains(harbor.get.propertyPane()))
    facilitiesVBox.children.add(harbor.get.propertyPane())

    if (hasGasStation && !facilitiesVBox.children.contains(gasStation.get.propertyPane()))
    facilitiesVBox.children.add(gasStation.get.propertyPane())

    styleLabels(14)

    factories.foreach(factory => {
      if (!factoriesVBox.children.contains(factory.propertyPane()))
      factoriesVBox.children.add(factory.propertyPane())
    })


    mainPanel
  }

}
