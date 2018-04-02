package logic.items.transport.vehicules

object VehicleComponentTypes {

  abstract class VehicleComponentType {}

  abstract class CarriageType extends VehicleComponentType {}
  abstract class EngineType extends VehicleComponentType {}

  case object RESOURCE_CARRIAGE extends CarriageType
  case object PASSENGER_CARRIAGE extends CarriageType

  case object DIESEL_ENGINE extends EngineType
  case object ELECTRIC_ENGINE extends EngineType
  case object KEROSENE_ENGINE extends EngineType

}
