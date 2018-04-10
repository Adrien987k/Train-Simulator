package logic.items.transport.facilities

import logic.Updatable
import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.{Highway, Rail}
import logic.items.transport.vehicules.VehicleTypes.TruckType
import logic.world.Company
import logic.world.towns.Town

class GasStation
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends TransportFacility(transportFacilityType, company, town, _capacity) with Updatable {

  def connectHighway(highway : Highway) : Unit = {
    super.connectRoad(highway)
  }

  def buildTruck(truckType : TruckType) : Boolean = {
    buildVehicle(truckType)
    true
  }

  def availableTrucks : Int = super.availableVehicles

}
