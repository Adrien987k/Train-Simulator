package logic.items.transport.vehicules.components

import logic.items.ItemTypes.ItemType

object VehicleComponentTypes {

  abstract class VehicleComponentType(name : String) extends ItemType(name) {}

  abstract class CarriageType(name : String) extends VehicleComponentType(name) {}
  abstract class EngineType(name : String) extends VehicleComponentType(name) {}

  abstract class ResourceCarriageType(name : String) extends CarriageType(name) {}
  abstract class PassengerCarriageType(name : String) extends CarriageType(name) {}

  case object RESOURCE_CARRIAGE extends ResourceCarriageType("Resources carriage")

  case object TRAIN_PASSENGER_CARRIAGE extends PassengerCarriageType("Passenger carriage")
  case object CONCORDE_PASSENGER_CARRIAGE extends PassengerCarriageType("Passenger Carriage")
  case object BOEING_PASSENGER_CARRIAGE extends PassengerCarriageType("Passenger Carriage")

  case object DIESEL_ENGINE extends EngineType("Diesel engine")
  case object ELECTRIC_ENGINE extends EngineType("Electric engine")
  case object KEROSENE_ENGINE extends EngineType("Kerosene engine")
  case object SHIP_ENGINE extends EngineType("Ship engine")

}
