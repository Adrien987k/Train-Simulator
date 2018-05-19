package logic.items.transport.vehicules

import logic.items.ItemTypes.ItemType

object VehicleTypes {

  abstract class VehicleType(name : String, price : Double) extends ItemType(name, price) {}

  abstract class TrainType(name : String, price : Double) extends VehicleType(name, price) {}
  abstract class PlaneType(name : String, price : Double) extends VehicleType(name, price) {}
  abstract class TruckType(name : String, price : Double) extends VehicleType(name, price) {}
  abstract class ShipType(name : String, price : Double) extends VehicleType(name, price) {}

  case object DIESEL_TRAIN extends TrainType("Diesel Train", 200)
  case object ELECTRIC_TRAIN extends TrainType("Electric Train", 300)

  case object BOEING extends PlaneType("Boeing", 1000)
  case object CONCORDE extends PlaneType("Concorde", 2000)

  case object TRUCK extends TruckType("Truck", 75)

  case object LINER extends ShipType("Liner", 600)
  case object CRUISE_BOAT extends ShipType("Cruise boat", 1500)

}
