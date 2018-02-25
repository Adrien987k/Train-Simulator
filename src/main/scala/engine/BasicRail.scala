package engine

import utils.Pos

class BasicRail(_stationA : Station, _stationB : Station) extends Rail(_stationA : Station, _stationB : Station) {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }

  override def info(): String = {
    "Max capacity : " + DEFAULT_CAPACITY + "\n\n" +
    "Trains : " + trains.size
  }

  override def pos: Pos = stationA.pos

  override def addTrain(train: Train): Unit = {
    trains += train
  }

  override def removeTrain(train: Train): Unit = {
    trains -= train
  }
}
