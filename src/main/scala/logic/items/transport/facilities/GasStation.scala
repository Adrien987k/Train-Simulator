package logic.items.transport.facilities

import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
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

  def buildTruck(truckType : TruckType) : Result = {
    buildVehicle(truckType)
  }

}
