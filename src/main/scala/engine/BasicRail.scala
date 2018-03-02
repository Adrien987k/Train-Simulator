package engine

import utils.Pos

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicRail(_stationA : Station, _stationB : Station) extends Rail(_stationA : Station, _stationB : Station) {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }


  override def pos: Pos = stationA.pos

  override def addTrain(train: Train): Unit = {
    trains += train
  }

  override def removeTrain(train: Train): Unit = {
    trains -= train
  }

  override def toString: String = {
    "Max capacity : " + DEFAULT_CAPACITY + "\n\n" +
      "Trains : " + trains.size
  }

  override def propertyPane(): Node = {
    val panel = new VBox()
    val maxCapLabel = new Label("Maw capacity : " + DEFAULT_CAPACITY)
    val nbTrainLabel = new Label("Trains : " + trains.size)
    val lengthLabel = new Label("Length : " + length.toInt)
    panel.children = List(maxCapLabel, nbTrainLabel, lengthLabel)
    panel
  }
}
