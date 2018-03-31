package logic.items.transport.vehicules

abstract class Engine
(val maxSpeed : Double,
 val maxWeight : Double,
 val maxTractiveEffort : Double,
 val maxFuelLevel : Double)
  extends VehicleComponent(maxSpeed, maxWeight) {

  private var _fuelLevel : Double = 0

  def fuelLevel : Double = _fuelLevel

  def refillFuelLevel() : Unit = {
    _fuelLevel = maxFuelLevel
  }

  def consume(weight : Double) : Unit = {
    _fuelLevel -= consumption(weight)
  }


  def tractiveEffort(weight : Double) : Double

  def consumption(weight : Double) : Double

}
