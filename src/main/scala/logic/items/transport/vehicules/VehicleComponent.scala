package logic.items.transport.vehicules

import logic.NotDisplayedUpdatable
import logic.items.transport.vehicules.VehicleComponentTypes.VehicleComponentType

abstract class VehicleComponent
(val vehicleComponentType : VehicleComponentType,
 var maxSpeed : Double,
 var maxWeight : Double) extends NotDisplayedUpdatable {

  val NO_CHANGE : Double = -1

  var _level = 1

  def level : Int = _level
  def level_= (value : Int) : Unit = _level = value

  def evolve() : Unit = {
    VehicleComponentFactory.evolve(this)
  }

  def evolve(newMaxSpeed : Double,
             newMaxWeight : Double) : Unit = {
    maxSpeed = if (newMaxSpeed == NO_CHANGE) maxSpeed else newMaxSpeed
    maxWeight = if (newMaxWeight == NO_CHANGE) maxWeight else newMaxWeight
  }

}
