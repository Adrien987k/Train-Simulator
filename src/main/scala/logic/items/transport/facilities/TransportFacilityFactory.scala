package logic.items.transport.facilities

import logic.items.ItemTypes.{AIRPORT, STATION, TransportFacilityType}
import logic.world.Company
import logic.world.towns.Town

object TransportFacilityFactory {

  def make(tfType : TransportFacilityType, company : Company, town : Town) : TransportFacility = {
    tfType match {
      case STATION => new BasicStation(STATION, company, town)
      case AIRPORT => new BasicAirport(AIRPORT, company, town)
    }
  }

}
