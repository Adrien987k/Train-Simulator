package logic.items.transport.facilities

import logic.items.ItemTypes.TransportFacilityType
import logic.world.Company
import logic.world.towns.Town

class BasicHarbor
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends Harbor(transportFacilityType, company, town, _capacity) {


}
