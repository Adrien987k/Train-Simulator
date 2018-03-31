package logic.items.transport.vehicules

import logic.items.transport.vehicules.VehicleComponentType.EngineType

class ElectricEngine
(override val engineType : EngineType,
 override val maxSpeed: Double,
 override val maxWeight: Double,
 override val maxTractiveEffort: Double,
 override val maxFuelLevel: Double)
  extends Engine(engineType, maxSpeed, maxWeight, maxTractiveEffort, maxFuelLevel) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 1000) return maxSpeed
    if (weight < 5000) 2.0
    else 1.0
  }

  override def consumption(weight : Double) : Double = {
    val speed = tractiveEffort(weight)

    weight * 0.001 * speed
  }

}
