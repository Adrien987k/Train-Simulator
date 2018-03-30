package logic.items.transport.roads

import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.Station
import logic.world.Company

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicRail
(override val roadType : RoadType,
 override val company: Company,
 override val stationA : Station,
 override val stationB : Station,
 override val speedLimit : Double)
  extends Rail(roadType, company, stationA , stationB, speedLimit) {

  val panel = new VBox()

  val maxCapLabel = new Label("Max capacity : " + DEFAULT_CAPACITY)
  val nbTrainLabel = new Label()
  val lengthLabel = new Label()
  val connectLabel = new Label()

  labels = List(maxCapLabel, nbTrainLabel, lengthLabel, connectLabel)

  panel.children = labels

  styleLabels()

  override def propertyPane(): Node = {
    maxCapLabel.text = "Max capacity : " + DEFAULT_CAPACITY
    nbTrainLabel.text = "Trains : " + nbTrain
    lengthLabel.text = "Length : " + length.toInt
    connectLabel.text = "Endpoints : " + stationA.town.name + ", " + stationB.town.name

    panel
  }

}
