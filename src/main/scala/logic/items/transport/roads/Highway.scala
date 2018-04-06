package logic.items.transport.roads

import logic.Updatable
import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.GasStation
import logic.items.transport.vehicules.Truck
import logic.world.Company

class Highway
(override val roadType : RoadType,
 override val company: Company,
 val gasStationA : GasStation,
 val gasStationB : GasStation,
 override val speedLimit : Double)
  extends Road(roadType, company, gasStationA, gasStationB, speedLimit) with Updatable {

  def nbTrucks : Int = super.nbVehicle

  def addTruck(truck : Truck) : Unit = super.addVehicle(truck)

  def removeTruck(truck : Truck) : Unit = super.removeVehicle(truck)

}
