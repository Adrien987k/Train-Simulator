package logic.items

import logic.items.VehicleCategories.VehicleCategory
import logic.items.transport.facilities.TransportFacilityTypes._
import logic.items.transport.roads.RoadTypes._
import logic.items.transport.vehicules.VehicleTypes._

object ItemTypes {

  abstract class ItemType(val name : String, val price : Double) {}

  abstract class FacilityType(name : String, price : Double) extends ItemType(name, price)

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

  def onSaleItemsForVehicleCategory(vehicleCategory : VehicleCategory) : List[ItemType] = {
    vehicleCategory match {
      case VehicleCategories.Trains => List(DIESEL_TRAIN, ELECTRIC_TRAIN, RAIL, STATION)
      case VehicleCategories.Planes => List(BOEING, CONCORDE, AIRPORT)
      case VehicleCategories.Ships => List(CRUISE_BOAT, LINER, HARBOR)
      case VehicleCategories.Trucks => List(TRUCK, HIGHWAY, GAS_STATION)

      case _ => List()
    }
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
