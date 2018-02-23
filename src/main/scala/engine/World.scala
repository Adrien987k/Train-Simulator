package engine

import interface.GUI

import scala.collection.mutable.ArrayBuffer

object World {

  val MAP_WIDTH = 1000
  val MAP_HEIGHT = 600

  val INIT_NB_TOWNS = 5

  val towns: ArrayBuffer[Town] = ArrayBuffer.empty
  val trains: ArrayBuffer[Train] = ArrayBuffer.empty
  val company: Company = new Company

  var running = true

  def init(): Unit = {
    //for (i = 0; i < INIT_NB_TOWNS; i++) {
      //Générer INIT_NB_TOWNS positions cohérentes et créer les villes à ces positions
      //towns.+=(new BasicTown(// GENERATED POS //))
    //}
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
