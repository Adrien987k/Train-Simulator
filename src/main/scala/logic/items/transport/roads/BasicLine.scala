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

  if (panel.children.contains(maxCapLabel))
    panel.children.remove(maxCapLabel)

}
