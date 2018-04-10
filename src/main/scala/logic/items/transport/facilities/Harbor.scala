package logic.items.transport.facilities

import game.Game
import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.RoadTypes.WATERWAY
import logic.items.transport.roads.Waterway
import logic.items.transport.vehicules.VehicleTypes.ShipType
import logic.world.Company
import logic.world.towns.Town

class Harbor
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends TransportFacility(transportFacilityType, company, town, _capacity) {

  Game.world.naturalWaterways.foreach(waterway => {
    if (waterway.townA == town || waterway.townB == town) {

      if (waterway.townA == town && waterway.townB.hasHarbor)
        company.buildRoad(WATERWAY, this, waterway.townB.harbor.get)

      if (waterway.townB == town && waterway.townA.hasHarbor)
        company.buildRoad(WATERWAY, this, waterway.townA.harbor.get)
    }
  })

  def connectWaterway(waterway : Waterway) : Unit = {
    super.connectRoad(waterway)
  }

  def buildShip(shipType : ShipType) : Boolean = {
    buildVehicle(shipType)
    true
  }

  def availableShips : Int = super.availableVehicles

}
