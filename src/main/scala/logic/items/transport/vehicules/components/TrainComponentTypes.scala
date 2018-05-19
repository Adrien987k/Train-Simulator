package logic.items.transport.vehicules.components

import logic.items.ItemTypes.ItemType

object TrainComponentTypes {

  abstract class TrainComponentType(name : String, price : Double) extends ItemType(name, price) {}

  abstract class CarriageType(name : String, price : Double) extends TrainComponentType(name, price) {}
  abstract class EngineType(name : String, price : Double) extends TrainComponentType(name, price) {}

  abstract class ResourceCarriageType(name : String, price : Double) extends CarriageType(name, price) {}
  abstract class PassengerCarriageType(name : String, price : Double) extends CarriageType(name, price) {}

  case object RESOURCE_CARRIAGE extends ResourceCarriageType("Resources carriage", 200)

  case object PASSENGER_CARRIAGE extends PassengerCarriageType("Passenger carriage", 200)

  case object DIESEL_ENGINE extends EngineType("Diesel engine", 150)
  case object ELECTRIC_ENGINE extends EngineType("Electric engine", 100)

}
