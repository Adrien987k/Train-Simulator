package logic.items.transport.facilities

import logic.items.ItemTypes.{ShipType, TransportFacilityType}
import logic.items.transport.roads.Waterway
import logic.world.Company
import logic.world.towns.Town

class Harbor
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends TransportFacility(transportFacilityType, company, town, _capacity) {

  def connectWaterway(waterway : Waterway) : Unit = {
    super.addRoad(waterway)
  }

  def buildShip(shipType : ShipType) : Boolean = {
    buildVehicle(shipType)
    true
  }

  def availableShips : Int = super.availableVehicles

}
