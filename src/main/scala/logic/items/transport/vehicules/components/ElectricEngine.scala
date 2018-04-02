package logic.items.transport.vehicules.components

import logic.items.transport.vehicules.components.VehicleComponentTypes.EngineType
import logic.world.Company

class ElectricEngine
(override val engineType : EngineType,
 override val company : Company,
 _maxSpeed : Double,
 _weight : Double,
 _maxTractiveEffort : Double,
 _maxFuelLevel : Double)
  extends Engine(engineType, company, _maxSpeed, _weight, _maxTractiveEffort, _maxFuelLevel) {

  override def tractiveEffort(totalWeight : Double) : Double = {
    if (maxTractiveEffort < 10000.0) return maxSpeed
    if (maxTractiveEffort < 50000.0) maxSpeed / 2.0
    else maxSpeed / 3.0
  }

  override def consumption(weight : Double) : Double = {
    val speed = tractiveEffort(weight)

    weight * 0.001 * speed
  }

}
