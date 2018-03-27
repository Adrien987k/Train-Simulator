package logic.items.transport.roads

import logic.Updatable
import logic.items.transport.facilities.Airport
import logic.items.transport.vehicules.Plane
import logic.world.Company

abstract class Line
(override val company : Company,
 val airportA : Airport,
 val airportB : Airport)
  extends Road(company, airportA, airportB) with Updatable {

  def nbPlane : Int = super.nbVehicle

  def addPlane(plane : Plane) : Unit = super.addVehicle(plane)

  def removePlane(plane : Plane) : Unit = super.removeVehicle(plane)

}
