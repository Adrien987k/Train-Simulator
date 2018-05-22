package logic.items.transport.vehicules.components

import logic.items.transport.vehicules.components.TrainComponentTypes.PASSENGER_CARRIAGE
import logic.world.Company

import scala.xml.Node

class PassengerCarriage
(override val company : Company,
 override val evolutionPlan : CarriageEvolutionPlan)
extends Carriage(PASSENGER_CARRIAGE, company, evolutionPlan) {

  var maxCapacity : Int = evolutionPlan.level(level)(2).toInt

  var nbPassenger = 0

  override def load(node: Node): Unit = {

  }

  override def save: Node = {
      <PassengerCarriage level={level.toString} passenger={nbPassenger.toString}/>
  }

  def loadPassenger(newPassenger : Int) : Int = {
    nbPassenger = math.max(newPassenger, maxCapacity)
    nbPassenger
  }

  def unloadPassenger() : Int = {
    val tempPassenger = nbPassenger
    nbPassenger = 0
    tempPassenger
  }

  override def evolve() : Unit = {
    super.evolve()

    maxCapacity = evolutionPlan.level(level)(2).toInt

    company.buy(evolvePrice)
  }

}
