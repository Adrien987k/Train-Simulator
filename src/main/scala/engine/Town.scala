package engine

import utils.Pos

import scala.collection.mutable.ArrayBuffer

abstract class Town(pos : Pos) extends Updatable {

  val name : String = ""
  val MAX_POPULATION = 1000000

  val stations : ArrayBuffer[Station] = ArrayBuffer.empty
  var offer : Offer = new Offer
  var request : Request = new Request

  var population = 0//Genérer

  override def getPos(): Pos = {
    pos
  }

  abstract def addStation(station : Station)

  abstract def produce()

  abstract def unload()

  abstract def load()

  abstract def explore()

}
