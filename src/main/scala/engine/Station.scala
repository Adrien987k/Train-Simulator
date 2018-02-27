package engine

import utils.Pos

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

abstract class Station(_pos : Pos, _town : Town) extends Item with Updatable {

  val DEFAULT_CAPACITY = 5

  var capacity : Int = DEFAULT_CAPACITY

  var waitingPassengers : mutable.HashMap[Station, Int] = mutable.HashMap.empty

  val trains:ListBuffer[Train] = ListBuffer.empty
  val rails:ListBuffer[Rail] = ListBuffer.empty

  def pos: Pos = _pos
  def town: Town = _town

  def addRail(rail : Rail): Unit

  def buildTrain(): Boolean

  def isFull: Boolean = trains.lengthCompare(capacity) == 0

  def neighbours(): ListBuffer[Station]
  def nbNeighbours(): Int = rails.size

  def sendPassenger(objective : Station, nbPassenger : Int) : Boolean

  def getRailTo(station : Station): Option[Rail]

  def unload(train : Train)

  def load(train : Train, objective : Station, nbPassenger : Int)

}
