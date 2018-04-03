package logic.items

object ItemTypes {

  abstract class ItemType(val name : String) {}

  abstract class VehicleType(name : String) extends ItemType(name) {}
  abstract class TrainType(name : String) extends VehicleType(name) {}
  abstract class PlaneType(name : String) extends VehicleType(name) {}

  abstract class TransportFacilityType(name : String) extends ItemType(name) {}

  abstract class RoadType(name : String) extends ItemType(name) {}

  case object DIESEL_TRAIN extends TrainType("Diesel Train")
  case object ELECTRIC_TRAIN extends TrainType("Electric Train")

  case object BOEING extends PlaneType("Boeing")
  case object CONCORDE extends PlaneType("Concorde")

  case object RAIL extends RoadType("Rail")
  case object LINE extends RoadType("Line")

  case object STATION extends TransportFacilityType("Station")
  case object AIRPORT extends TransportFacilityType("Airport")

  def items() : List[ItemType] = {
    List(DIESEL_TRAIN, ELECTRIC_TRAIN, RAIL, LINE, STATION, AIRPORT)
  }

  def onSaleItems() : List[ItemType] = {
    List(BOEING, CONCORDE, DIESEL_TRAIN, ELECTRIC_TRAIN, RAIL, STATION, AIRPORT)
  }

  def transportFacilityFromVehicle(vehicleType : VehicleType) : TransportFacilityType = {
    vehicleType match {
      case _ : TrainType => STATION
      case _ : PlaneType => AIRPORT
    }
  }

}
