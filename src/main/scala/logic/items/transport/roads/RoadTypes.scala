package logic.items.transport.roads

import logic.items.ItemTypes.ItemType

object RoadTypes {

  abstract class RoadType(name : String, price : Double) extends ItemType(name, price) {}

  case object RAIL extends RoadType("Rail", 2.0)
  case object LINE extends RoadType("Line", 0.0)
  case object WATERWAY extends RoadType("Waterway", 0.0)
  case object HIGHWAY extends RoadType("Highway", 1.0)

}
