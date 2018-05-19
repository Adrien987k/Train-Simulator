package logic.items.transport.roads

import logic.items.transport.facilities.Station
import logic.items.transport.roads.RoadTypes.RoadType
import logic.world.Company

class Rail
(override val roadType : RoadType,
 override val company: Company,
 val stationA : Station,
 val stationB : Station,
 override val evolutionPlan: RoadEvolutionPlan)
  extends Road(roadType, company, stationA, stationB, evolutionPlan) {

}
