package logic.items.transport.roads

import logic.Updatable
import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.Airport
import logic.items.transport.vehicules.Plane
import logic.world.Company

class Line
(override val roadType : RoadType,
 override val company : Company,
 val airportA : Airport,
 val airportB : Airport)
  extends Road(roadType, company, airportA, airportB, Double.MaxValue) with Updatable {

  if (panel.children.contains(maxCapLabel))
    panel.children.remove(maxCapLabel)

  def nbPlane : Int = super.nbVehicle

  def addPlane(plane : Plane) : Unit = super.addVehicle(plane)

  def removePlane(plane : Plane) : Unit = super.removeVehicle(plane)

}
