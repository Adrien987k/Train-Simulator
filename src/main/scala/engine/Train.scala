package engine

abstract class Train extends Item with Updatable {

  val DEFAULT_SPEED = 10
  val DEFAULT_SIZE = 10
  val DEFAULT_MAX_WEIGHT = 100

  var speed : Int = DEFAULT_SPEED
  var size : Int = DEFAULT_SIZE
  var maxWeight : Int = DEFAULT_MAX_WEIGHT

  abstract def render()

}
