package logic.items.transport.roads

import logic.items.ItemTypes.{LINE, RAIL, RoadType}
import logic.items.transport.facilities.{Airport, Station, TransportFacility}
import logic.world.Company

object RoadFactory {

  def makeRoad(roadType : RoadType,
                company : Company,
               transportFacilityA : TransportFacility,
               transportFacilityB : TransportFacility) : Road = {
    roadType match {
      case RAIL =>
        new BasicRail(RAIL, company,
                      transportFacilityA.asInstanceOf[Station],
                      transportFacilityB.asInstanceOf[Station],
                      10.0)
      case LINE =>
        new BasicLine(LINE, company,
          transportFacilityA.asInstanceOf[Airport],
          transportFacilityB.asInstanceOf[Airport])
    }
  }

}
