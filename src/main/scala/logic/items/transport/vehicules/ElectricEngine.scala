package logic.items.transport.vehicules

class ElectricEngine extends Engine(3.0, 10000.0, 50000.0) {

  override def tractiveEffort(weight: Double): Double = {
    if (weight < 1000) return maxWeight
    if (weight < 5000) 2.0
    else 1.0
  }

}
