package logic.items.transport.facilities

import logic.items.transport.facilities.TransportFacilityTypes._
import logic.world.Company
import logic.world.towns.Town

object TransportFacilityFactory {

  def make(tfType : TransportFacilityType, company : Company, town : Town) : TransportFacility = {
    tfType match {
      case STATION =>
        new Station(STATION, company, town,
          TransportFacilityEvolutionPlan(
            List(20, 25, 35, 50, 100), 1500, 500
          ))

      case AIRPORT => new Airport(AIRPORT, company, town,
        TransportFacilityEvolutionPlan(
          List(10, 15, 25), 2000, 2000
        ))

      case HARBOR => new Harbor(HARBOR, company, town,
        TransportFacilityEvolutionPlan(
          List(5, 10, 15, 30), 1500, 1000
        ))

      case GAS_STATION => new GasStation(GAS_STATION, company, town,
        TransportFacilityEvolutionPlan(
          List(25, 50, 75, 100), 500, 250
        ))
    }
  }

}
