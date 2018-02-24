package engine

import utils.Pos

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

abstract class Town(pos : Pos) extends Updatable {

  val name : String = ""
  val MAX_POPULATION = 1000000
  val MIN_POPULATION = 10

  val stations : ArrayBuffer[Station] = ArrayBuffer.empty
  var offer : Offer = new Offer
  var request : Request = new Request

  var population : Int = MIN_POPULATION +
      new Random().nextInt(MAX_POPULATION + 1 - MIN_POPULATION)


  def getPos(): Pos = pos

  def addStation(station : Station)

  def produce()

  def unload(train : Train)

  def load(train : Train, from : Station, to : Station, nbPassenger : Int)

  def explore()

}
