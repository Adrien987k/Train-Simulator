package logic.items.transport.facilities
import logic.items.transport.facilities.TransportFacilityTypes.TransportFacilityType
import logic.items.transport.roads.RoadTypes.LINE
import logic.items.transport.vehicules.VehicleTypes.PlaneType
import logic.world.Company
import logic.world.towns.Town

class Airport
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 override val evolutionPlan : TransportFacilityEvolutionPlan)
  extends TransportFacility(transportFacilityType, company, town, evolutionPlan) {

  company.getAirports.foreach(airport =>
    company.buildRoad(LINE, this, airport)
  )

  def buildPlane(planeType : PlaneType) : Boolean = {
    buildVehicle(planeType)
    true
  }

}
