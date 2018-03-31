package logic.items.transport.vehicules

class ElectricEngine
(override val maxSpeed: Double,
 override val maxWeight: Double,
 override val maxTractiveEffort: Double,
 override val maxFuelLevel: Double)
  extends Engine(maxSpeed, maxWeight, maxTractiveEffort, maxFuelLevel) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 1000) return maxSpeed
    if (weight < 5000) 2.0
    else 1.0
  }

  override def consumption(weight : Double) : Double = {
    weight * 0.1
  }

}
