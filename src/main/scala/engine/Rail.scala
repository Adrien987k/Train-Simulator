package engine

import scala.collection.mutable.ArrayBuffer

abstract class Rail(company: Company, _stationA : Station, _stationB : Station) extends Item(company: Company) with Updatable {

  val DEFAULT_CAPACITY = 3

  val capacity : Int = DEFAULT_CAPACITY
  val length : Double = _stationA.pos.dist(_stationB.pos)

  val trains: ArrayBuffer[Train] = ArrayBuffer.empty

  def stationA : Station = _stationA
  def stationB : Station = _stationB

  def nbTrain : Int = trains.size
  def isFull : Boolean = capacity == trains.size

  def addTrain(train : Train): Unit

  def removeTrain(train : Train): Unit
}
