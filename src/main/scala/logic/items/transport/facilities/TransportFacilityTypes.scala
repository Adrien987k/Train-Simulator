package logic.items.transport.facilities

import logic.items.ItemTypes.FacilityType

object TransportFacilityTypes {

  abstract class TransportFacilityType(name : String, price : Double) extends FacilityType(name, price) {}

  case object STATION extends TransportFacilityType("Station", 2000)
  case object AIRPORT extends TransportFacilityType("Airport", 5000)
  case object HARBOR extends TransportFacilityType("Harbor", 4000)
  case object GAS_STATION extends TransportFacilityType("Gas Station", 1000)

}
