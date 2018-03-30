package logic.items.transport.roads

import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.Airport
import logic.world.Company

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicLine
(override val roadType : RoadType,
 override val company: Company,
 override val airportA : Airport,
 override val airportB : Airport)
  extends Line(roadType, company, airportA , airportB) {

  val panel = new VBox()

  val maxCapLabel = new Label("Max capacity : " + DEFAULT_CAPACITY)
  val nbPlaneLabel = new Label()
  val lengthLabel = new Label()
  val connectLabel = new Label()

  labels = List(maxCapLabel, nbPlaneLabel, lengthLabel, connectLabel)

  panel.children = labels

  styleLabels()

  override def propertyPane(): Node = {
    maxCapLabel.text = "Max capacity : " + DEFAULT_CAPACITY
    nbPlaneLabel.text = "Trains : " + nbVehicle
    lengthLabel.text = "Length : " + length.toInt
    connectLabel.text = "Endpoints : " + airportA.town.name + ", " + airportB.town.name

    panel
  }

}
