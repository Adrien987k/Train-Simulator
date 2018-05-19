package logic.items.transport.vehicules.components

import logic.NotDisplayedUpdatable
import logic.items.{EvolutionPlan, Item}
import logic.items.transport.vehicules.components.TrainComponentTypes.TrainComponentType
import logic.world.Company

abstract class TrainComponent
(val componentType : TrainComponentType,
 override val company : Company,
 override val evolutionPlan : EvolutionPlan)
  extends Item(componentType, company, evolutionPlan) with NotDisplayedUpdatable {

  private var _maxSpeed : Double = evolutionPlan.level(level).head
  private var _weight : Double = evolutionPlan.level(level)(1)

  def maxSpeed : Double = _maxSpeed
  def weight : Double = _weight

  override def evolve() : Unit = {
    super.evolve()

    _maxSpeed = evolutionPlan.level(level).head
    _weight = evolutionPlan.level(level)(1)
  }

}
