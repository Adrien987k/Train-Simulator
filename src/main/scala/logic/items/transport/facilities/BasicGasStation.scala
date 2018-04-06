package logic.items.transport.facilities

import logic.items.ItemTypes.TransportFacilityType
import logic.world.Company
import logic.world.towns.Town

class BasicGasStation
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends GasStation(transportFacilityType, company, town, _capacity) {


}
