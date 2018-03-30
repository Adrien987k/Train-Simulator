package logic.items.transport.vehicules

class KeroseneEngine
(maxSpeed: Double,
 maxWeight: Double,
 maxTractiveEffort: Double)
  extends Engine(maxSpeed, maxWeight, maxTractiveEffort) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 2500) return maxSpeed
    if (weight < 10000) maxSpeed / 2
    else maxSpeed / 3
  }

}
