package logic.items.transport.facilities
import logic.Updatable
import logic.items.ItemTypes.{LINE, PlaneType, TransportFacilityType}
import logic.items.transport.roads.Line
import logic.world.Company
import logic.world.towns.Town

abstract class Airport
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
extends TransportFacility(transportFacilityType, company, town, _capacity) with Updatable {

  company.getAirports.foreach(airport =>
    company.buildRoad(LINE, this, airport)
  )

  def connectLine(line : Line) : Unit = {
    super.connectRoad(line)
  }

  def buildPlane(planeType : PlaneType) : Boolean = {
    buildVehicle(planeType)
    true
  }

  def availablePlanes() : Int = super.availableVehicles

}
