package logic.items

object ItemTypes {

  abstract class ItemType {}

  abstract class VehicleType extends ItemType {}
  abstract class TrainType extends VehicleType {}

  abstract class TransportFacilityType extends ItemType {}
  abstract class Station extends TransportFacilityType {}

  abstract class RoadType extends ItemType {}
  abstract class RailType extends RoadType {}

  case object DIESEL_TRAIN extends TrainType
  case object ELECTRIC_TRAIN extends TrainType

}
