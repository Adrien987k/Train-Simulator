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
          RoadEvolutionPlan(
            List(10.0, 12.0),
            List(3, 5, 10), 50.0, 50.0
          ))

      case LINE =>
        new Line(LINE, company,
          transportFacilityA.asInstanceOf[Airport],
          transportFacilityB.asInstanceOf[Airport],
          RoadEvolutionPlan(
            List(Double.MaxValue),
            List(Double.MaxValue), 0.0, 0.0
          ))

      case WATERWAY =>
        new Waterway(WATERWAY, company,
          transportFacilityA.asInstanceOf[Harbor],
          transportFacilityB.asInstanceOf[Harbor],
          RoadEvolutionPlan(
            List(8.0),
            List(2, 3, 5), 50.0, 50.0
          ))

      case HIGHWAY =>
        new Highway(HIGHWAY, company,
          transportFacilityA.asInstanceOf[GasStation],
          transportFacilityB.asInstanceOf[GasStation],
          RoadEvolutionPlan(
            List(10.0, 12.0),
            List(10, 15, 20), 10.0, 10.0
          ))

    }
  }

}
