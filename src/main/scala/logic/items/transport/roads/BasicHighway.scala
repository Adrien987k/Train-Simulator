package logic.items.transport.roads

import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.GasStation
import logic.world.Company

class BasicHighway
(override val roadType : RoadType,
 override val company : Company,
 override val gasStationA : GasStation,
 override val gasStationB : GasStation,
 override val speedLimit : Double)
  extends Highway(roadType, company, gasStationA , gasStationB, speedLimit) {

}
