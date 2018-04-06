package logic.items.transport.facilities

import logic.items.ItemTypes.TransportFacilityType
import logic.world.Company
import logic.world.towns.Town

class BasicAirport
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends Airport(transportFacilityType, company, town, _capacity) {

}