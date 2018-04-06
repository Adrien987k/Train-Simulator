package logic.items

object ItemTypes {

  abstract class ItemType(val name : String) {}

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

  abstract class TransportFacilityType(name : String) extends ItemType(name) {}

  case object STATION extends TransportFacilityType("Station")
  case object AIRPORT extends TransportFacilityType("Airport")
  case object HARBOR extends TransportFacilityType("Harbor")
  case object GAS_STATION extends TransportFacilityType("Gas Station")

  abstract class RoadType(name : String) extends ItemType(name) {}

  case object RAIL extends RoadType("Rail")
  case object LINE extends RoadType("Line")
  case object WATERWAY extends RoadType("Waterway")
  case object HIGHWAY extends RoadType("Highway")

  def items() : List[ItemType] = {
    List(DIESEL_TRAIN, ELECTRIC_TRAIN, RAIL, LINE, STATION, AIRPORT)
  }

  def onSaleItems() : List[ItemType] = {
    List(BOEING,
      CONCORDE,
      DIESEL_TRAIN,
      ELECTRIC_TRAIN,
      TRUCK,
      LINER,
      CRUISE_BOAT,
      RAIL,
      HIGHWAY,
      STATION,
      AIRPORT,
      HARBOR,
      GAS_STATION)
  }

  def transportFacilityFromVehicle(vehicleType : VehicleType) : TransportFacilityType = {
    vehicleType match {
      case _ : TrainType => STATION
      case _ : PlaneType => AIRPORT
      case _ : ShipType => HARBOR
      case _ : TruckType => GAS_STATION
    }
  }

  def transportFacilityTypeFromRoadType(roadType : RoadType) : TransportFacilityType = {
    roadType match {
      case RAIL => STATION
      case LINE => AIRPORT
      case WATERWAY => HARBOR
      case HIGHWAY => GAS_STATION
    }
  }

}
