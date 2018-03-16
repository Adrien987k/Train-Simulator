package logic.items

object ItemTypes {

  abstract class ItemType(val name : String) {}

  abstract class VehicleType(name : String) extends ItemType(name) {}
  abstract class TrainType(name : String) extends VehicleType(name) {}

  abstract class TransportFacilityType(name : String) extends ItemType(name) {}
  abstract class StationType(name : String) extends TransportFacilityType(name) {}

  abstract class RoadType(name : String) extends ItemType(name) {}
  abstract class RailType(name : String) extends RoadType(name) {}

  case object DIESEL_TRAIN extends TrainType("Diesel Train")
  case object ELECTRIC_TRAIN extends TrainType("Electric Train")

  case object RAIL extends RailType("Rail")
  case object STATION extends StationType("Station")

  def items() : List[ItemType] = {
    List(DIESEL_TRAIN, ELECTRIC_TRAIN, RAIL, STATION)
  }

}
