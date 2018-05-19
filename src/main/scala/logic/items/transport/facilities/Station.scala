package logic.items.transport.facilities

import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.vehicules.VehicleTypes.TrainType
import logic.world.Company
import logic.world.towns.Town

class Station
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 override val evolutionPlan : TransportFacilityEvolutionPlan)
  extends TransportFacility(transportFacilityType, company, town, evolutionPlan) {

  def buildTrain(trainType : TrainType) : Boolean = {
    buildVehicle(trainType)
    true
  }

}
