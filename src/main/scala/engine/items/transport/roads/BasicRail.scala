package engine.items.transport.roads

import engine.items.transport.facilities.Station
import engine.items.transport.vehicules.Train
import engine.world.Company
import utils.Pos

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicRail(company: Company, _stationA : Station, _stationB : Station) extends Rail(company: Company, _stationA : Station, _stationB : Station) {

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
    val maxCapLabel = new Label("Max capacity : " + DEFAULT_CAPACITY)
    val nbTrainLabel = new Label("Trains : " + trains.size)
    val lengthLabel = new Label("Length : " + length.toInt)
    val connectLabel = new Label("Endpoints : "
      + stationA.town.name + ", " + stationB.town.name)
    panel.children = List(maxCapLabel, nbTrainLabel, lengthLabel, connectLabel)
    panel
  }
}
