package engine

import interface.GUI
import utils.Pos

import scala.collection.mutable.ListBuffer
import scala.util.Random

object World {

  val MAP_WIDTH = 1000
  val MAP_HEIGHT = 1000

  val INIT_NB_TOWNS = 10

  val towns : ListBuffer[Town] = ListBuffer.empty
  val trains : ListBuffer[Train] = ListBuffer.empty
  val rails :  ListBuffer[Rail] = ListBuffer.empty
  val company: Company = new Company

  var running = true

  def init(): Unit = {
    val rand = new Random
    val areaWidth = MAP_WIDTH / INIT_NB_TOWNS
    for (i <- 0 to INIT_NB_TOWNS) {
      val x = rand.nextInt(areaWidth) + (i * areaWidth)
      val y = rand.nextInt(MAP_HEIGHT)
      towns += new BasicTown(new Pos(x, y), "Town " + i)
    }
    GUI.initWorld(towns)
  }

  def run(): Unit = {
    while (running) {
      for (town <- towns) {
        town.step()
      }
      //GUI.display()
    }
  }

}
