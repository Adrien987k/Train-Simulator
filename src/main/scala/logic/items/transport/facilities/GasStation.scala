package logic.items.transport.facilities

import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.Highway
import logic.items.transport.vehicules.VehicleTypes.TruckType
import logic.world.Company
import logic.world.towns.Town
import utils.Result

class GasStation
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 override val evolutionPlan : TransportFacilityEvolutionPlan)
  extends TransportFacility(transportFacilityType, company, town, evolutionPlan) {

  def connectHighway(highway : Highway) : Unit = {
    super.connectRoad(highway)
  }

  def buildTruck(truckType : TruckType) : Result = {
    buildVehicle(truckType)
  }

  def availableTrucks : Int = super.availableVehicles

}
