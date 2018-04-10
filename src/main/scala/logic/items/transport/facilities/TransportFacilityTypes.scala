package logic.items.transport.facilities

import logic.items.ItemTypes.FacilityType

object TransportFacilityTypes {

  abstract class TransportFacilityType(name : String) extends FacilityType(name) {}

  case object STATION extends TransportFacilityType("Station")
  case object AIRPORT extends TransportFacilityType("Airport")
  case object HARBOR extends TransportFacilityType("Harbor")
  case object GAS_STATION extends TransportFacilityType("Gas Station")

}
