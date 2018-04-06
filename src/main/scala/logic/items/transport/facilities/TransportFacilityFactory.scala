package logic.items.transport.facilities

import logic.items.ItemTypes._
import logic.world.Company
import logic.world.towns.Town

object TransportFacilityFactory {

  private val DEFAULT_CAPACITY = 5

  def make(tfType : TransportFacilityType, company : Company, town : Town) : TransportFacility = {
    tfType match {
      case STATION => new BasicStation(STATION, company, town, DEFAULT_CAPACITY)

      case AIRPORT => new BasicAirport(AIRPORT, company, town, DEFAULT_CAPACITY)

      case HARBOR => new BasicHarbor(HARBOR, company, town, DEFAULT_CAPACITY)

      case GAS_STATION => new GasStation(GAS_STATION, company, town, DEFAULT_CAPACITY)
    }
  }

}
