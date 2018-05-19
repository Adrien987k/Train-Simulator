package logic.items.transport.roads

import logic.items.transport.facilities.Airport
import logic.items.transport.roads.RoadTypes.RoadType
import logic.world.Company

class Line
(override val roadType : RoadType,
 override val company : Company,
 val airportA : Airport,
 val airportB : Airport,
 override val evolutionPlan: RoadEvolutionPlan)
  extends Road(roadType, company, airportA, airportB, evolutionPlan) {

  if (panel.children.contains(maxCapLabel))
    panel.children.remove(maxCapLabel)

}
