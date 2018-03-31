package logic.items.transport.vehicules

class DieselEngine
(override val maxSpeed : Double,
 override val maxWeight : Double,
 override val maxTractiveEffort : Double,
 override val maxFuelLevel: Double)
  extends Engine(maxSpeed, maxWeight, maxTractiveEffort, maxFuelLevel) {

  override def tractiveEffort(weight : Double): Double = {
    if (weight < 2000) return maxSpeed
    if (weight < 10000) 3.0
    else 1.5
  }

  override def consumption(weight : Double) : Double = {
    weight
  }

}
