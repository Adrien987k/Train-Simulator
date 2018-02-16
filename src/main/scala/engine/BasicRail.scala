package engine

class BasicRail extends Rail {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }

  override def info(): String = {
    "Max capacity : " + DEFAULT_CAPACITY + "\n\n" +
    "Trains : " + currentNbTrains
  }
}
