package engine.world

import engine.Updatable
import engine.items.ItemType
import engine.world.towns.{BasicTown, Town}
import interface.{GUI, GlobalInformationPanel, WorldCanvas}
import link.{CreationChange, Observable}
import utils.{Pos, Timer}

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scalafx.animation.AnimationTimer

object World extends Observable {

  val MAP_WIDTH = 700
  val MAP_HEIGHT = 700

  val INIT_NB_TOWNS = 10

  var towns: ListBuffer[Town] = ListBuffer.empty
  var company: Company = new BasicCompany

  var timer: Timer = new Timer

  def init(): Unit = {
    val rand = new Random
    val areaWidth = MAP_WIDTH / INIT_NB_TOWNS
    for (i <- 0 to INIT_NB_TOWNS) {
      val x = rand.nextInt(areaWidth - WorldCanvas.TOWN_RADIUS * 2) + (i * areaWidth) + WorldCanvas.TOWN_RADIUS
      val y = rand.nextInt(MAP_HEIGHT - WorldCanvas.TOWN_RADIUS * 2) + WorldCanvas.TOWN_RADIUS
      towns += new BasicTown(new Pos(x, y), "Town " + i)
    }

    GUI.initWorldCanvas(towns)

    val timer = AnimationTimer { _ =>
      update()
      GlobalInformationPanel.update()
      WorldCanvas.update()
    }
    timer.start()
  }

  def update(): Unit = {
    timer.update()
    for (town <- towns) {
      town.step()
    }
    for (train <- company.trains) {
      train.step()
    }
    company.trains.foreach(train => addChange(new CreationChange(train.pos, null, ItemType.TRAIN)))
    notifyObservers()
  }

  def newGame(): Unit = {
    towns = ListBuffer.empty
    company = new BasicCompany
    timer.restart()
    GUI.restart()
    init()
  }

  def updatableAt(pos : Pos): Option[Updatable] = {
    val town = towns.find(town => town.pos.inRange(pos, WorldCanvas.TOWN_RADIUS * 1.7))
    if (town.nonEmpty) return town
    val train = company.trains.find(train => train.pos.inRange(pos, WorldCanvas.TRAIN_RADIUS))
    if (train.nonEmpty) return train
    company.rails.find(rail => pos.inLineRange(rail.stationA.pos, rail.stationB.pos, 10))
  }


  def totalPopulation(): Int = {
    val inTowns = towns.foldLeft(0)((total, town) => total + town.population + town.nbWaitingPassengers)
    val inTrains = company.trains.foldLeft(0)((total, train) => total + train.nbPassenger)
    inTowns + inTrains
  }

}
