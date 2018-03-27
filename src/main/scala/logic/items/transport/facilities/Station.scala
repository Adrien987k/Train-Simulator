package logic.items.transport.facilities

import logic.Updatable
import logic.items.ItemTypes.TrainType
import logic.items.transport.roads.Rail
import logic.world.Company
import logic.world.towns.Town

abstract class Station
(company: Company,
 town : Town)
  extends TransportFacility(company, town) with Updatable {

  def addRail(rail : Rail): Unit = {
    super.addRoad(rail)
  }

  def buildTrain(trainType : TrainType) : Boolean = {
    buildVehicle(trainType)
    true
  }

  def availableTrains() : Int = super.availableVehicles

}
