package logic.items.transport.facilities
import logic.Updatable
import logic.items.ItemTypes.PlaneType
import logic.items.transport.roads.Line
import logic.world.Company
import logic.world.towns.Town

abstract class Airport
(company : Company,
town : Town)
extends TransportFacility(company, town) with Updatable {

  def addLine(line : Line) : Unit = {
    super.addRoad(line)
  }

  def buildPlane(planeType : PlaneType) : Boolean = {
    buildVehicle(planeType)
    true
  }

  def availablePlanes() : Int = super.availableVehicles

}