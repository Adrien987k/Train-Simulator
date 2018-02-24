package engine

import utils.Pos

abstract class Rail(pos1 : Pos, pos2 : Pos) extends Item with Updatable {

  val DEFAULT_CAPACITY = 3

  var currentNbTrains = 0

}
