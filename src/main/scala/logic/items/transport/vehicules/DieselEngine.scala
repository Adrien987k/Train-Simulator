package logic.items.transport.vehicules

import logic.items.transport.vehicules.VehicleComponentTypes.EngineType

class DieselEngine
(override val engineType : EngineType,
 maxSpeed : Double,
 maxWeight : Double,
 maxTractiveEffort : Double,
 maxFuelLevel: Double)
  extends Engine(engineType, maxSpeed, maxWeight, maxTractiveEffort, maxFuelLevel) {

  override def tractiveEffort(weight : Double) : Double = {
    println("W: " + weight)
    println("MT : " + maxTractiveEffort)
    println("MS : " + maxSpeed)

    if (weight < maxTractiveEffort / 3) return maxSpeed
    if (weight < maxTractiveEffort / 2) return maxSpeed / 2
    if (weight < maxTractiveEffort) return maxSpeed / 4

    maxSpeed / 15
  }

  override def consumption(weight : Double) : Double = {
    val speed = tractiveEffort(weight)

    weight * 0.01 * speed
  }

}
