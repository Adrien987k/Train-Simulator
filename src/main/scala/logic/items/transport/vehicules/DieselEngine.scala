package logic.items.transport.vehicules

class DieselEngine extends Engine(5.0, 20000.0, 50000.0) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 2000) return maxWeight
    if (weight < 10000) 3.0
    else 1.5
  }

}
