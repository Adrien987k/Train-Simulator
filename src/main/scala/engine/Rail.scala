package engine

abstract class Rail extends Item with Updatable {

  val DEFAULT_CAPACITY = 3

  var currentNbTrains = 0

}
