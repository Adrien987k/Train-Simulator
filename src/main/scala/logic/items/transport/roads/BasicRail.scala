package logic.items.transport.roads

import logic.items.transport.facilities.Station
import logic.world.Company

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicRail
(override val company: Company,
 override val stationA : Station,
 override val stationB : Station)
  extends Rail(company, stationA , stationB) {

  override def propertyPane(): Node = {
    val panel = new VBox()
    val maxCapLabel = new Label("Max capacity : " + DEFAULT_CAPACITY)
    val nbTrainLabel = new Label("Trains : " + nbTrain)
    val lengthLabel = new Label("Length : " + length.toInt)
    val connectLabel = new Label("Endpoints : "
      + stationA.town.name + ", " + stationB.town.name)
    panel.children = List(maxCapLabel, nbTrainLabel, lengthLabel, connectLabel)
    panel
  }

}
