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

  override def propertyPane(): Node = {
    val panel = new VBox()
    val maxCapLabel = new Label("Max capacity : " + DEFAULT_CAPACITY)
    val nbPlaneLabel = new Label("Trains : " + nbVehicle)
    val lengthLabel = new Label("Length : " + length.toInt)
    val connectLabel = new Label("Endpoints : "
      + airportA.town.name + ", " + airportB.town.name)
    panel.children = List(maxCapLabel, nbPlaneLabel, lengthLabel, connectLabel)
    panel
  }

}
