package engine

import interface.{GUI, WorldCanvas}
import utils.Pos

import scala.collection.mutable.ListBuffer
import scala.util.Random

object World {

  val MAP_WIDTH = 800
  val MAP_HEIGHT = 700

  val INIT_NB_TOWNS = 10

  var towns: ListBuffer[Town] = ListBuffer.empty
  var company: Company = new Company

  def init(): Unit = {
    val rand = new Random
    val areaWidth = MAP_WIDTH / INIT_NB_TOWNS
    for (i <- 0 to INIT_NB_TOWNS) {
      val x = rand.nextInt(areaWidth) + (i * areaWidth)
      val y = rand.nextInt(MAP_HEIGHT)
      towns += new BasicTown(new Pos(x, y), "Town " + i)
    }
    GUI.initWorldCanvas(towns)
  }

  def newGame(): Unit = {
    towns = ListBuffer.empty
    company = new Company
    GUI.restart()
    init()
  }

  def updatableAt(pos : Pos): Option[Updatable] = {
    towns.find(town => town.pos.inRange(pos, WorldCanvas.TOWN_RADIUS))
  }

  def update(): Unit = {
    for (town <- towns) {
      town.step()
    }
    for (train <- company.trains) {
      train.step()
    }
  }

}
