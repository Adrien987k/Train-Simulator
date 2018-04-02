package logic.items.transport.vehicules.components

import logic.items.transport.vehicules.components.VehicleComponentTypes.EngineType
import logic.world.Company

class KeroseneEngine
(override val engineType : EngineType,
 override val company : Company,
 _maxSpeed : Double,
 _weight : Double,
 _maxTractiveEffort : Double,
 _maxFuelLevel : Double)
  extends Engine(engineType, company, _maxSpeed, _weight, _maxTractiveEffort, _maxFuelLevel) {

  override def tractiveEffort(totalWeight : Double) : Double = {
    if (totalWeight < maxTractiveEffort / 3.0) return maxSpeed
    if (totalWeight < maxTractiveEffort / 2.0) return maxSpeed / 1.5
    if (totalWeight < maxTractiveEffort) return maxSpeed / 2.0

    maxSpeed / 3.0
  }

  override def consumption(weight : Double) : Double = {
    val speed = tractiveEffort(weight)

    weight * 0.5 * speed
  }

}
