package logic.items.transport.roads

import logic.Updatable
import logic.items.transport.facilities.Harbor
import logic.items.transport.roads.RoadTypes.RoadType
import logic.world.Company

class Waterway
(override val roadType : RoadType,
 override val company: Company,
 val harborA : Harbor,
 val harborB : Harbor,
 override val evolutionPlan: RoadEvolutionPlan)
  extends Road(roadType, company, harborA, harborB, evolutionPlan) {

}
