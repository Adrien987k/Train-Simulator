package logic.items.transport.vehicules

class DieselEngine
(maxSpeed: Double,
 maxWeight: Double,
 maxTractiveEffort: Double)
  extends Engine(maxSpeed, maxWeight, maxTractiveEffort) {

  override def tractiveEffort(weight : Double): Double = {
    if (weight < 2000) return maxWeight
    if (weight < 10000) 3.0
    else 1.5
  }

}
