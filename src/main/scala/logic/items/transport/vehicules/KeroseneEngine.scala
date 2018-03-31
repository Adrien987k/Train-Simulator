package logic.items.transport.vehicules

class KeroseneEngine
(override val maxSpeed : Double,
 override val maxWeight : Double,
 override val maxTractiveEffort : Double,
 override val maxFuelLevel: Double)
  extends Engine(maxSpeed, maxWeight, maxTractiveEffort, maxFuelLevel) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 2500) return maxSpeed
    if (weight < 10000) maxSpeed / 2
    else maxSpeed / 3
  }

  override def consumption(weight : Double) : Double = {
    weight * 5.0
  }

}
