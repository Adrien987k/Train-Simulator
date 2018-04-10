package logic.items.transport.facilities

import logic.Updatable
import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.Rail
import logic.items.transport.vehicules.VehicleTypes.TrainType
import logic.world.Company
import logic.world.towns.Town

class Station
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends TransportFacility(transportFacilityType, company, town, _capacity) with Updatable {

  def connectRail(rail : Rail) : Unit = {
    super.connectRoad(rail)
  }

  def buildTrain(trainType : TrainType) : Boolean = {
    buildVehicle(trainType)
    true
  }

  def availableTrains : Int = super.availableVehicles

}
