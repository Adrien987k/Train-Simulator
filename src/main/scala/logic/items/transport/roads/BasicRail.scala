package logic.items.transport.roads

import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.Station
import logic.world.Company

class BasicRail
(override val roadType : RoadType,
 override val company : Company,
 override val stationA : Station,
 override val stationB : Station,
 override val speedLimit : Double)
  extends Rail(roadType, company, stationA , stationB, speedLimit) {

}
