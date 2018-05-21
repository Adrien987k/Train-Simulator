package logic.world

import logic.{Loadable, Updatable}
import logic.world.towns.Town
import interface.{GUI, ItemsStyle}
import logic.items.transport.facilities.TransportFacilityFactory
import logic.items.transport.facilities.TransportFacilityTypes.{AIRPORT, GAS_STATION, HARBOR, STATION}
import logic.items.transport.roads.RoadTypes.{HIGHWAY, LINE, RAIL, WATERWAY}
import utils.{DateTime, Pos}

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scalafx.animation.AnimationTimer

class World() extends Loadable{

  class NaturalWaterway
  (val townA : Town,
   val townB : Town) { }

  private val MAP_WIDTH = 2000
  private val MAP_HEIGHT = 2000

  private val INIT_NB_TOWNS = 50
  private val APPARITION_WATERWAY = 0.5

  private val rand = new Random

  var towns : ListBuffer[Town] = ListBuffer.empty
  var naturalWaterways : ListBuffer[NaturalWaterway] = ListBuffer.empty

  var company : Company = new Company(this)

  val gameDateTime : DateTime = new DateTime

  def start() : Unit = {
    init()

    GUI.initWorldCanvas(towns)

    val timer = AnimationTimer { _ =>
      update()

      GUI.update()

      Thread.sleep(50)
    }

    timer.start()
  }

  def init() : Unit = {
    towns = ListBuffer.empty
    naturalWaterways = ListBuffer.empty

    company = new Company(this)

    generateRandomMap()

    gameDateTime.restart()
  }

  override def load(node : scala.xml.Node) : Unit = {
    loadWorld(node)

    GUI.initWorldCanvas(towns)

    val timer = AnimationTimer { _ =>
      update()

      GUI.update()

      Thread.sleep(50)
    }

    timer.start()
  }

  def update() : Unit = {
    gameDateTime.update()

    company.step()
    towns.foreach(_.step())
  }

  def time() : DateTime = gameDateTime.copy()

  def loadWorld(node : scala.xml.Node) : Unit = {
    towns = ListBuffer.empty
    naturalWaterways = ListBuffer.empty

    company = new Company(this)

    for (<Money>{money}</Money> <- node \"Money") {company._money = money.text.toDouble}

    generateLoadMap(node)
    gameDateTime.load(node \"Date")
  }

  /**
    * @return True if there exists a natural waterway between [townA] and [townB]
    */
  def existNaturalWaterWay(townA : Town, townB : Town) : Boolean = {
    naturalWaterways.exists(waterway => {
      (waterway.townA == townA && waterway.townB == townB) ||
      (waterway.townB == townA && waterway.townA == townB)
    })
  }

  /**
    * Generate a random new map
    */
  def generateRandomMap() : Unit = {
    val areaWidth = MAP_WIDTH / INIT_NB_TOWNS

    for (i <- 0 to INIT_NB_TOWNS) {
      val x = rand.nextInt(areaWidth - ItemsStyle.INIT_TOWN_SIZE * 2) + (i * areaWidth) + ItemsStyle.INIT_TOWN_SIZE
      val y = rand.nextInt(MAP_HEIGHT - ItemsStyle.INIT_TOWN_SIZE * 2) + ItemsStyle.INIT_TOWN_SIZE
      towns += new Town(new Pos(x, y), "Town " + i)
    }

    for (townA <- towns) {
      for (townB <- towns) {
        if (townA != townB) {
          val randDouble = rand.nextDouble() * 100.0

          if (randDouble < APPARITION_WATERWAY && townA != townB && !existNaturalWaterWay(townA, townB)) {
            naturalWaterways += new NaturalWaterway(townA, townB)
          }
        }
      }
    }
  }

  def loadNaturalWaterway(nameTownA : String, nameTownB : String) : Unit = {
    towns.foreach(townA => if (nameTownA == townA.name) {towns.foreach(townB => if (nameTownB == townB.name) {naturalWaterways += new NaturalWaterway(townA,townB)})} )
  }

  def loadConnection(road : scala.xml.Node, townA : Town, townB : Town) : Unit = {
    road match {
      case <Rail></Rail> =>
        val transportFacilityA = TransportFacilityFactory.make(STATION, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(STATION, company, townB)
        company.buildRoad(RAIL, transportFacilityA, transportFacilityB)

      case <Line></Line> =>
        val transportFacilityA = TransportFacilityFactory.make(AIRPORT, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(AIRPORT, company, townB)
        company.buildRoad(LINE, transportFacilityA, transportFacilityB)

      case <Waterway></Waterway> =>
        val transportFacilityA = TransportFacilityFactory.make(HARBOR, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(HARBOR, company, townB)
        company.buildRoad(WATERWAY, transportFacilityA, transportFacilityB)

      case <Highway></Highway> =>
        val transportFacilityA = TransportFacilityFactory.make(GAS_STATION, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(GAS_STATION, company, townB)
        company.buildRoad(HIGHWAY, transportFacilityA, transportFacilityB)
    }
  }

  def generateLoadMap(node : scala.xml.Node) : Unit = {
    node match {
      case <Map>{subnode @ _*}</Map> =>
        for (city @ <City>{_*}</City> <- subnode) {
          val x = (city \"@x").text.toDouble
          val y = (city \"@y").text.toDouble
          val name = (city \"@name").text
          val population = (city \"@population").text.toInt
          val town = new Town(new Pos(x, y), name)
          town.initPopulation(population)
          town.loadCity(city, company)
          towns += town
        }
        for (waterway @ <NaturalWaterway>{_*}</NaturalWaterway> <- subnode) {
          val nameTownA = (waterway \"@townA").text
          val nameTownB = (waterway \"@townB").text
          loadNaturalWaterway(nameTownA, nameTownB)
        }
        for (<Connection>{road @ _*}</Connection> <- subnode) {
          for (nodeRoad <- road) {
            val nameTownA = (nodeRoad \"@townA").text
            val nameTownB = (nodeRoad \"@townB").text
            towns.foreach(townA => if (nameTownA == townA.name) {towns.foreach(townB => if (nameTownB == townB.name) {loadConnection(nodeRoad, townA, townB)})} )
          }
        }
    }
  }


  /**
    * Try to find an element at the position [pos] in the world map
    *
    * @param pos The position where to search
    * @return An option with some element if one is found none otherwise
    */
  def updatableAt(pos : Pos) : Option[Updatable] = {
    val town = towns.find(town => {
      val style = ItemsStyle.ofTown(town)

      town.pos.inRange(pos, style.radius * 1.7)
    })
    if (town.nonEmpty) return town

    val vehicle = company.vehicles.find(vehicle => {
      val style = ItemsStyle.ofVehicle(vehicle.vehicleType)
      vehicle.pos.inRange(pos, style.radius)
    })

    if (vehicle.nonEmpty) return vehicle

    company.roads.find(road => {
      val style = ItemsStyle.ofRoad(road.roadType)

      !style.empty && pos.inLineRange(road.transportFacilityA.pos, road.transportFacilityB.pos, 10)
    })
  }


  /**
    * @return The total population size of the world
    */
  def totalPopulation() : Int = {
    val inTowns = towns.foldLeft(0)((total, town) => total + town.population + town.nbWaitingPassengers)
    val inTrains = company.vehicles.foldLeft(0)((total, vehicle) => total + vehicle.nbPassenger())
    inTowns + inTrains
  }

}
