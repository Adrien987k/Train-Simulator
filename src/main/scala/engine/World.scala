package engine

import interface.GUI

import scala.collection.mutable.ArrayBuffer

object World {

  val towns: ArrayBuffer[Town] = ArrayBuffer.empty
  val trains: ArrayBuffer[Train] = ArrayBuffer.empty
  val company: Company = new Company

  var running = true

  def init(): Unit = {

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
