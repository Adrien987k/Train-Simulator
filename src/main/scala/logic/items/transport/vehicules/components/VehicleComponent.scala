package logic.items.transport.vehicules.components

import interface.GlobalInformationPanel
import logic.NotDisplayedUpdatable
import logic.exceptions.AlreadyMaxLevelException
import logic.items.transport.vehicules.components.VehicleComponentTypes.VehicleComponentType
import logic.world.Company

abstract class VehicleComponent
(val vehicleComponentType : VehicleComponentType,
 val company : Company,
 private var _maxSpeed : Double,
 private var _weight : Double) extends NotDisplayedUpdatable {

  val NO_CHANGE : Double = -1

  var _level = 1

  def maxSpeed : Double = _maxSpeed
  def weight : Double = _weight

  def level : Int = _level
  def level_= (value : Int) : Unit = _level = value

  def evolve() : Unit = {
    if (!company.canBuyEvolution(vehicleComponentType, level)) {
      GlobalInformationPanel.displayWarningMessage("Not enough money")

      return
    }

    try {
      VehicleComponentFactory.evolve(this)
    } catch {
      case e : AlreadyMaxLevelException =>
        throw new AlreadyMaxLevelException(e.getMessage)
    }

    company.buyEvolution(vehicleComponentType, level)
  }

  def evolve(newMaxSpeed : Double,
             newMaxWeight : Double) : Unit = {
    _maxSpeed = if (newMaxSpeed == NO_CHANGE) maxSpeed else newMaxSpeed
    _weight = if (newMaxWeight == NO_CHANGE) weight else newMaxWeight
  }

}
