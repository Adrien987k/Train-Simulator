package logic.items.transport.roads

import logic.items.ItemTypes.ItemType

object RoadTypes {

  abstract class RoadType(name : String) extends ItemType(name) {}

  case object RAIL extends RoadType("Rail")
  case object LINE extends RoadType("Line")
  case object WATERWAY extends RoadType("Waterway")
  case object HIGHWAY extends RoadType("Highway")

}
