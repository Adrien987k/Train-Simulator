package logic.items.transport.roads

import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.Harbor
import logic.world.Company

class BasicWaterway
(override val roadType : RoadType,
 override val company : Company,
 override val harborA : Harbor,
 override val harborB : Harbor,
 override val speedLimit : Double)
  extends Waterway(roadType, company, harborA , harborB, speedLimit) {


}
