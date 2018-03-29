package logic.items.transport.roads

import logic.Updatable
import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.Station
import logic.items.transport.vehicules.Train
import logic.world.Company

abstract class Rail
(override val roadType : RoadType,
 override val company: Company,
 val stationA : Station,
 val stationB : Station,
 override val speedLimit : Double)
  extends Road(roadType, company, stationA, stationB, speedLimit) with Updatable {

  def nbTrain : Int = super.nbVehicle

  def addTrain(train : Train) : Unit = super.addVehicle(train)

  def removeTrain(train : Train) : Unit = super.removeVehicle(train)

}
