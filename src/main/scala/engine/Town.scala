package engine

import scala.collection.mutable.ArrayBuffer

abstract class Town extends Updatable {

  val name : String = ""

  val stations : ArrayBuffer[Station] = ArrayBuffer.empty
  var offer : Offer = new Offer
  var request : Request = new Request

  abstract def produce()

  abstract def unload()

  abstract def load()

  abstract def explore()

}
