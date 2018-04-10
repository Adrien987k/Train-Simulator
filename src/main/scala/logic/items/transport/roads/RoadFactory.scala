package logic.items.transport.roads

import logic.items.transport.facilities._
import logic.items.transport.roads.RoadTypes._
import logic.world.Company

object RoadFactory {

  def make(roadType : RoadType,
           company : Company,
           transportFacilityA : TransportFacility,
           transportFacilityB : TransportFacility) : Road = {

    roadType match {
      case RAIL =>
        new Rail(RAIL, company,
                      transportFacilityA.asInstanceOf[Station],
                      transportFacilityB.asInstanceOf[Station],
                      10.0)
      case LINE =>
        new Line(LINE, company,
          transportFacilityA.asInstanceOf[Airport],
          transportFacilityB.asInstanceOf[Airport])

      case WATERWAY =>
        new Waterway(WATERWAY, company,
          transportFacilityA.asInstanceOf[Harbor],
          transportFacilityB.asInstanceOf[Harbor],
          5.0)

      case HIGHWAY =>
        new Highway(HIGHWAY, company,
          transportFacilityA.asInstanceOf[GasStation],
          transportFacilityB.asInstanceOf[GasStation],
          7.5)

    }
  }

}
