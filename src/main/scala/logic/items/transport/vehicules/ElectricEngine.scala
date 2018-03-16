package logic.items.transport.vehicules

class ElectricEngine
(maxSpeed: Double,
 maxWeight: Double,
 maxTractiveEffort: Double)
  extends Engine(maxSpeed, maxWeight, maxTractiveEffort) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 1000) return maxWeight
    if (weight < 5000) 2.0
    else 1.0
  }

}
