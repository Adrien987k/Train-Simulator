package logic.items.transport.vehicules

import logic.items.ItemTypes.ItemType

object VehicleTypes {

  abstract class VehicleType(name : String) extends ItemType(name) {}

  abstract class TrainType(name : String) extends VehicleType(name) {}
  abstract class PlaneType(name : String) extends VehicleType(name) {}
  abstract class TruckType(name : String) extends VehicleType(name) {}
  abstract class ShipType(name : String) extends VehicleType(name) {}

  case object DIESEL_TRAIN extends TrainType("Diesel Train")
  case object ELECTRIC_TRAIN extends TrainType("Electric Train")

  case object BOEING extends PlaneType("Boeing")
  case object CONCORDE extends PlaneType("Concorde")

  case object TRUCK extends TruckType("Truck")

  case object LINER extends ShipType("Liner")
  case object CRUISE_BOAT extends ShipType("Cruise boat")

}
