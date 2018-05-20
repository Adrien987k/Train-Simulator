package logic.items.transport.facilities
import game.Game
import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.RoadTypes.LINE
import logic.items.transport.vehicules.VehicleTypes.PlaneType
import logic.world.Company
import logic.world.towns.Town
import utils.Result

class Airport
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 override val evolutionPlan : TransportFacilityEvolutionPlan)
  extends TransportFacility(transportFacilityType, company, town, evolutionPlan) {

  company.getAirports.foreach(airport =>
    if (!company.roadAlreadyExists(town, airport.town) &&
      !Game.world.existNaturalWaterWay(town, airport.town))
      company.buildRoad(LINE, this, airport)
  )

  def buildPlane(planeType : PlaneType) : Result = {
    buildVehicle(planeType)
  }

}
