package logic.items.transport.vehicules.components

import logic.items.transport.vehicules.components.VehicleComponentTypes.EngineType
import logic.world.Company

class ShipEngine
(override val engineType : EngineType,
 override val company : Company,
 _maxSpeed : Double,
 _weight : Double,
 _maxTractiveEffort : Double,
 _maxFuelLevel : Double)
  extends Engine(engineType, company, _maxSpeed, _weight, _maxTractiveEffort, _maxFuelLevel) {

  override def tractiveEffort(totalWeight : Double) : Double = {
    maxSpeed
  }

  override def consumption(weight : Double) : Double = {
    weight * 0.005
  }

}
