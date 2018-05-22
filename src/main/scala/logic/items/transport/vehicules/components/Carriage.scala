package logic.items.transport.vehicules.components

import logic.Loadable
import logic.items.EvolutionPlan
import logic.items.transport.vehicules.components.TrainComponentTypes.CarriageType
import logic.world.Company

import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

case class CarriageEvolutionPlan
(maxSpeeds : List[Double],
 weight : List[Double],
 capacities : List[Double])
  extends EvolutionPlan(
    List(maxSpeeds, weight, capacities), 15.0, 15.0)

abstract class Carriage
(val carriageType : CarriageType,
 override val company : Company,
 override val evolutionPlan : EvolutionPlan)
  extends TrainComponent(carriageType, company, evolutionPlan) with Loadable {

  override def step() : Boolean = { false }

  override def save : scala.xml.Node = {
    this match {
      case resourceCarriage : ResourceCarriage => resourceCarriage.save
      case passengerCarriage : PassengerCarriage => passengerCarriage.save
    }
  }

  override def propertyPane() : Node = { new BorderPane }

}
