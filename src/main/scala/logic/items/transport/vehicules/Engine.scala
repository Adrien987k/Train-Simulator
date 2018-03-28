package logic.items.transport.vehicules

abstract class Engine
(val maxSpeed : Double,
 val maxWeight : Double,
 val maxTractiveEffort : Double)
  extends VehicleComponent(maxSpeed, maxWeight) {

  def tractiveEffort(weight : Double) : Double

}
