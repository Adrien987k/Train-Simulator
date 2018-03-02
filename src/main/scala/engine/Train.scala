package engine

import utils.{Dir, Pos}

abstract class Train(company: Company, _pos : Pos) extends Item(company : Company) with Updatable {

  val DEFAULT_SPEED = 1
  val DEFAULT_SIZE = 120
  val DEFAULT_MAX_WEIGHT = 100

  var dir : Dir = new Dir(0, 0)
  var speed : Double = DEFAULT_SPEED
  var size : Int = DEFAULT_SIZE
  var maxWeight : Int = DEFAULT_MAX_WEIGHT

  var nbPassenger = 0

  var goalStation : Option[Station] = None
  var currentRail : Option[Rail] = None

  def pos: Pos = _pos

  def setObjective(station : Station, from : Pos)
  def unsetObjective()

  def putOnRail(rail : Rail): Boolean
  def removeFromRail(): Unit

}
