package logic.items.transport.vehicules

import logic.items.transport.vehicules.VehicleComponentType.EngineType

class KeroseneEngine
(override val engineType : EngineType,
 override val maxSpeed : Double,
 override val maxWeight : Double,
 override val maxTractiveEffort : Double,
 override val maxFuelLevel: Double)
  extends Engine(engineType, maxSpeed, maxWeight, maxTractiveEffort, maxFuelLevel) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 2500) return maxSpeed
    if (weight < 10000) maxSpeed / 2
    else maxSpeed / 3
  }

  override def consumption(weight : Double) : Double = {
    val speed = tractiveEffort(weight)

    weight * 0.5 * speed
  }

}
