package engine

import utils.Pos

import scala.collection.mutable.ArrayBuffer

abstract class Station(pos : Pos, town : Town) extends Item with Updatable {

  val DEFAULT_CAPACITY = 5

  val name = ""
  var capacity : Int = DEFAULT_CAPACITY

  val train:ArrayBuffer[Train] = ArrayBuffer.empty
  val rails:ArrayBuffer[Rail] = ArrayBuffer.empty

  def unload(train : Train)

  def load(train : Train, objective : Station, nbPassenger : Int)

}
