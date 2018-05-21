package logic.world.towns


import game.Game
import logic.economy.Resources.{BOXED, DRY_BULK, LIQUID, Resource}
import logic.{Loadable, PointUpdatable, UpdateRate}
import logic.economy._
import logic.exceptions.CannotBuildItemException
import logic.items.production.FactoryTypes.FactoryType
import logic.items.production.{Factory, FactoryFactory}
import logic.items.transport.facilities.TransportFacilityTypes._
import logic.items.transport.facilities._
import logic.items.transport.vehicules.VehicleTypes._
import logic.world.Company
import statistics._
import utils._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.scene.text.{Font, FontWeight}

class Town(_pos : Pos, private var _name : String) extends PointUpdatable with Loadable {

  updateRate(UpdateRate.TOWN_UPDATE)
  pos = _pos

  private val rand = new Random()

  val MAX_POPULATION = 1000000
  val MIN_POPULATION = 10
  val DEFAULT_PROPORTION_TRAVELER = 0.1
  val INIT_MAX_NB_FACTORY = 3

  var station : Option[Station] = None
  var airport : Option[Airport] = None
  var harbor : Option[Harbor] = None
  var gasStation : Option[GasStation] = None

  private var _population : Int = MIN_POPULATION +
      new Random().nextInt(MAX_POPULATION + 1 - MIN_POPULATION)

  private val proportionTraveler : Double = DEFAULT_PROPORTION_TRAVELER

  val factories : ListBuffer[Factory] = ListBuffer.empty

  private val maxNbFactory : Int = INIT_MAX_NB_FACTORY

  val offer : ResourceCollection = new ResourceCollection
  val requests : ResourceMap = new ResourceMap
  val consumption : ResourceMap = Consumption.initialConsumption()
  val warehouse : ResourceCollection = new ResourceCollection

  private val requestFromOtherCities = new ListBuffer[Request]

  val stats = new Statistics("city")

  def name : String = _name
  def population : Int = _population

  def hasStation : Boolean = station.nonEmpty
  def hasAirport : Boolean = airport.nonEmpty
  def hasHarbor : Boolean = harbor.nonEmpty
  def hasGasStation : Boolean = gasStation.nonEmpty

  def initPopulation (newPopulation : Int) : Unit = _population = newPopulation

  def loadWarehouse(node : scala.xml.NodeSeq) : Unit = {
    node match {
      case <Warehouse>{subnode @ _*}</Warehouse> =>
        for (resource @ <Resource>{_*}</Resource> <- subnode) {
          val resourceType = (resource \"@type").text
          val quantity = (resource \"@quantity").text.toInt
          val resourcePack = Resources.loadResource(resourceType, quantity)
          warehouse.storeResourcePack(resourcePack)
        }
    }
  }

  def loadOffer(node : scala.xml.NodeSeq) : Unit = {
    node match {
      case <Offer>{subnode @ _*}</Offer> =>
        for (resource @ <Resource>{_*}</Resource> <- subnode) {
          val resourceType = (resource \"@type").text
          val quantity = (resource \"@quantity").text.toInt
          val resourcePack = Resources.loadResource(resourceType, quantity)
          offer.storeResourcePack(resourcePack)
        }
    }
  }

  def loadRequest(node : scala.xml.NodeSeq) : Unit = {
    node match {
      case <Request>{subnode @ _*}</Request> =>
        for (resource @ <Resource>{_*}</Resource> <- subnode) {
          val resourceType = (resource \"@type").text
          val quantity = (resource \"@quantity").text.toInt
          Resources.addResourceMap(requests, resourceType, quantity)
        }
    }
  }

  def loadConsumption (node : scala.xml.NodeSeq) : Unit = {
    node match {
      case <Consumption>{subnode @ _*}</Consumption> =>
        for (resource @ <Resource>{_*}</Resource> <- subnode) {
          val resourceType = (resource \"@type").text
          val quantity = (resource \"@quantity").text.toInt
          Resources.addResourceMap(consumption, resourceType, quantity)
        }
    }
  }

  def loadStatistic (node : scala.xml.NodeSeq) : Unit = {
    node match {
      case <Statistic>{subnode @ _*}</Statistic> =>
        for (event @ <Event>{_*}</Event> <- subnode) {
          val day = (event \"@day").text.toInt
          val hour = (event \"@hour").text.toInt
          val date = new DateTime(day, hour)
          val name = (event \"@name").text
          val typeValue = (event \"@type").text
          val value = (event \"@value").text
        }
    }
  }

  def loadCity (node : scala.xml.Node, company: Company) : Unit = {
    node match {
      case <City>{subnode @ _*}</City> =>
        for (factory @ <Factory>{_*}</Factory> <- subnode) {
          val name = (factory \"@type").text
          var newFactory = FactoryFactory.loadFactory(name, company, this)
          newFactory.loadProduction(factory)
          factories += newFactory
        }
        for (warehouse @ <Warehouse>{_*}</Warehouse> <- subnode) loadWarehouse(warehouse)
        for (offer @ <Offer>{_*}</Offer> <- subnode) loadOffer(offer)
        for (request @ <Request>{_*}</Request> <- subnode) loadRequest(request)
        for (consumption @ <Consumption>{_*}</Consumption> <- subnode) loadConsumption(consumption)
        for (statistic @ <Statistic>{_*}</Statistic> <- subnode) loadStatistic(statistic)
    }

    if ((node \"Airport") != scala.xml.NodeSeq.Empty) buildTransportFacility(AIRPORT)
    if ((node \"Port") != scala.xml.NodeSeq.Empty) buildTransportFacility(HARBOR)
    if ((node \"GasStation") != scala.xml.NodeSeq.Empty) buildTransportFacility(GAS_STATION)
    if ((node \"Station") != scala.xml.NodeSeq.Empty) buildTransportFacility(STATION)
  }

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
  def buildTransportFacility(tfType : TransportFacilityType) : Unit = {
    def buildInternal(tfOpt : Option[TransportFacility], itemNameForError : String) : TransportFacility = {
      tfOpt match {
        case Some(_) =>
          throw new CannotBuildItemException("This town already have " + itemNameForError)

        case None =>
          val tf = TransportFacilityFactory.make(tfType, Game.world.company, this)
          Game.world.company.addTransportFacility(tf)
          stats.newEvent(tfType.name + " built")
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

  def buildFactory(factoryType : FactoryType) : Unit = {
    if (!Game.world.company.canBuy(factoryType.price))
      throw new CannotBuildItemException("Not enough money")

    if (factories.lengthCompare(maxNbFactory) == 0)
      throw new CannotBuildItemException("Max number of factory reached")

    factories += FactoryFactory.make(factoryType, Game.world.company, this)

    stats.newEvent(factoryType.name + " built")

    Game.world.company.buy(factoryType.price)
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
      case DIESEL_TRAIN | ELECTRIC_TRAIN =>
        if (!hasStation) Failure("This town does not have a Station")
        result = station.get.buildTrain(vehicleType.asInstanceOf[TrainType])

      case BOEING | CONCORDE =>
        if (!hasAirport) Failure("This town does not have an airport")
        result = airport.get.buildPlane(vehicleType.asInstanceOf[PlaneType])

      case LINER | CRUISE_BOAT =>
        if (!hasHarbor) Failure("This town does not have an harbor")
        result = harbor.get.buildShip(vehicleType.asInstanceOf[ShipType])

      case TRUCK =>
        if (!hasGasStation) Failure("This town does not have a gas station")
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
    * Send a list of resource packs to a town
    *
    * @param town where to send the resources
    * @param packs the packs to send
    */
  def sendResource(town : Town, packs : ListBuffer[ResourcePack]) : Unit = {
    //TODO do something with cargoes / Take them from somewhere
    val cargoDryBulk = new Cargo(DRY_BULK)
    val cargoLiquid = new Cargo(LIQUID)
    val cargoBoxed = new Cargo(BOXED)

    packs.foldLeft(None)((None, pack) => {
      pack.resource.resourceType match {
        case DRY_BULK => cargoDryBulk.store(ListBuffer(pack))
        case LIQUID => cargoLiquid.store(ListBuffer(pack))
        case BOXED => cargoBoxed.store(ListBuffer(pack))
      }

      None
    })

    val cargoes = ListBuffer(cargoDryBulk, cargoLiquid, cargoBoxed)

    transportFacilities().foreach(tfOpt => {
      tfOpt.foreach(tf => {

        if (neighboursOf(tfOpt).contains(town))
          tf.trySendCargoes(town.transportFacilityOfType(tf.transportFacilityType).get,
            cargoes) match {
            case Success() =>
              stats.newEvent("Request answered", town.name)

            case Failure(reason) =>

          }
      })
    })
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

  private def neighboursOf(transportFacilityOpt : Option[TransportFacility]) : ListBuffer[Town] = {
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

  def consume() : Unit = {
    consumption.resources.foreach(resourceAndQuantity => {
      val (resource, quantity) = resourceAndQuantity

      val (_, missingQuantity) = warehouse.take(resource, quantity)

      if (missingQuantity > 0) {
        stats.newEvent("City missing resource", new ResourcePack(resource, quantity))
      }

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

  def searchResource() : Unit = {
    transportFacilities().foreach(tfOpt => {
      tfOpt.foreach(searchResource)
    })
  }

  /**
    * Search the neighbours cities for missing resources in the city
    */
  def searchResource(transportFacility : TransportFacility) : Unit = {
    transportFacility.neighbours().foreach(tf => {
      requests.resources.foreach(request => {
        searchResourceIn(tf.town, request)
      })
    })
  }

  def searchResourceIn(town : Town, request : (Resource, Int)) : Unit = {
    if (town.offer.quantityOf(request._1) > 0) {

      town.takeRequestFromOtherTown(Request(this, request._1, request._2))
    }
  }

  def takeRequestFromOtherTown(request : Request) : Unit = {
    requestFromOtherCities += request

    stats.newEvent("Request received", request)
  }

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

      sendResource(town, packs)
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
