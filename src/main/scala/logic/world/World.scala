package logic.world

import logic.Updatable
import logic.world.towns.{BasicTown, Town}
import interface.{GUI, GlobalInformationPanel, WorldCanvas}
import link.Observable
import utils.{Pos, GameDateTime}

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scalafx.animation.AnimationTimer

class World() extends Observable {

  val MAP_WIDTH = 700
  val MAP_HEIGHT = 700

  val INIT_NB_TOWNS = 10

  var towns : ListBuffer[Town] = ListBuffer.empty
  var company : Company = new BasicCompany(this)

  var gameDateTime : GameDateTime = new GameDateTime

  def init() : Unit = {
    val rand = new Random
    val areaWidth = MAP_WIDTH / INIT_NB_TOWNS
    for (i <- 0 to INIT_NB_TOWNS) {
      val x = rand.nextInt(areaWidth - WorldCanvas.TOWN_RADIUS * 2) + (i * areaWidth) + WorldCanvas.TOWN_RADIUS
      val y = rand.nextInt(MAP_HEIGHT - WorldCanvas.TOWN_RADIUS * 2) + WorldCanvas.TOWN_RADIUS
      towns += new BasicTown(new Pos(x, y), "Town " + i)
    }

  }

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

  def update() : Unit = {
    gameDateTime.update()

    company.step()
    towns.foreach(_.step())

    notifyObservers()
  }

  def newGame() : Unit = {
    towns = ListBuffer.empty
    company = new BasicCompany(this)
    gameDateTime.restart()
    GUI.restart()
    start()
  }

  def updatableAt(pos : Pos) : Option[Updatable] = {
    val town = towns.find(town => town.pos.inRange(pos, WorldCanvas.TOWN_RADIUS * 1.7))
    if (town.nonEmpty) return town
    val train = company.vehicles.find(train => train.pos.inRange(pos, WorldCanvas.TRAIN_RADIUS))
    if (train.nonEmpty) return train
    company.roads.find(road =>
      pos.inLineRange(road.transportFacilityA.pos, road.transportFacilityB.pos, 10))
  }


  def totalPopulation() : Int = {
    val inTowns = towns.foldLeft(0)((total, town) => total + town.population + town.nbWaitingPassengers)
    val inTrains = company.vehicles.foldLeft(0)((total, vehicle) => total + vehicle.nbPassenger())
    inTowns + inTrains
  }

}
