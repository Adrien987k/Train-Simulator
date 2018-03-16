package logic.items.transport.vehicules

object VehicleComponentType {

  abstract class CarriageType {}
  abstract class EngineType {}

  case object RESOURCE_CARRIAGE extends CarriageType
  case object PASSENGER_CARRIAGE extends CarriageType

  case object DIESEL_ENGINE extends EngineType
  case object ELECTRIC_ENGINE extends EngineType

}
