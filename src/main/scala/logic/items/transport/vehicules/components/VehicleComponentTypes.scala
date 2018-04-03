package logic.items.transport.vehicules.components

import logic.items.ItemTypes.ItemType

object VehicleComponentTypes {

  abstract class VehicleComponentType(name : String) extends ItemType(name) {}

  abstract class CarriageType(name : String) extends VehicleComponentType(name) {}
  abstract class EngineType(name : String) extends VehicleComponentType(name) {}

  case object RESOURCE_CARRIAGE extends CarriageType("Resources carriage")
  case object PASSENGER_CARRIAGE extends CarriageType("Passenger carriage")

  case object DIESEL_ENGINE extends EngineType("Diesel engine")
  case object ELECTRIC_ENGINE extends EngineType("Electric engine")
  case object KEROSENE_ENGINE extends EngineType("Kerosene engine")

}
