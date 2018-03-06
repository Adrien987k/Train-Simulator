package engine

import utils.{Dir, Pos}

abstract class Train(company: Company, _pos : Pos) extends Item(company : Company) with Updatable {

  val DEFAULT_SPEED = 1
  val DEFAULT_SIZE = 50
  val DEFAULT_MAX_WEIGHT = 1000
  val DEFAULT_PASSENGER_CAPACITY = 500

  var dir : Dir = new Dir(0, 0)
  var speed : Double = DEFAULT_SPEED
  var size : Int = DEFAULT_SIZE
  var maxWeight : Int = DEFAULT_MAX_WEIGHT
  var passengerCapacity: Int = DEFAULT_PASSENGER_CAPACITY

  var nbPassenger = 0

  /* The next station to reach */
  var goalStation : Option[Station] = None

  /* The final town to reach */
  var destination : Option[Town] = None

  var currentRail : Option[Rail] = None

  def pos: Pos = _pos

  def setObjective(station : Station, from : Pos)
  def unsetObjective()
  def setDestination(town : Town)

  def putOnRail(rail : Rail): Boolean
  def removeFromRail(): Unit

}
