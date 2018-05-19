package logic.items.transport.roads

import logic.items.transport.facilities.GasStation
import logic.items.transport.roads.RoadTypes.RoadType
import logic.world.Company

class Highway
(override val roadType : RoadType,
 override val company: Company,
 val gasStationA : GasStation,
 val gasStationB : GasStation,
 override val evolutionPlan: RoadEvolutionPlan)
  extends Road(roadType, company, gasStationA, gasStationB, evolutionPlan) {

}
