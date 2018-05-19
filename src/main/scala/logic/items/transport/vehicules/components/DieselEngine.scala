package logic.items.transport.vehicules.components

import logic.items.transport.vehicules.components.TrainComponentTypes.EngineType
import logic.world.Company

class DieselEngine
(override val engineType : EngineType,
 override val company : Company,
 override val evolutionPlan : EngineEvolutionPlan)
  extends Engine(engineType, company, evolutionPlan) {

  override def tractiveEffort(totalWeight : Double) : Double = {
    if (maxTractiveEffort != 0)
      maxSpeed * (1 - (totalWeight / maxTractiveEffort))

    maxSpeed
  }

  override def consumption(weight : Double) : Double = {
    val speed = tractiveEffort(weight)

    weight * 0.001 * speed
  }

}
