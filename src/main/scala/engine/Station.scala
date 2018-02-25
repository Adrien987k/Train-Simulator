package engine

import utils.Pos

import scala.collection.mutable.ListBuffer

abstract class Station(_name : String, _pos : Pos, town : Town) extends Item with Updatable {

  val DEFAULT_CAPACITY = 5

  var capacity : Int = DEFAULT_CAPACITY

  var waitingPassengers = 0

  val trains:ListBuffer[Train] = ListBuffer.empty
  val rails:ListBuffer[Rail] = ListBuffer.empty

  def name: String = _name
  def pos: Pos = _pos

  def addRail(rail : Rail): Unit

  def addTrain(train : Train): Boolean

  def isFull: Boolean = trains.lengthCompare(capacity) == 0

  def neighbours(): ListBuffer[Station]
  def nbNeighbours(): Int = rails.size

  def sendPassenger(objective : Station, nbPassenger : Int)

  def getRailTo(station : Station): Option[Rail]

  def unload(train : Train)

  def load(train : Train, objective : Station, nbPassenger : Int)

}
