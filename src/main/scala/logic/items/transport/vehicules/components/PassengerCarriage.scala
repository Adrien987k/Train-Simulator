package logic.items.transport.vehicules.components

import logic.items.transport.vehicules.components.VehicleComponentTypes.PASSENGER_CARRIAGE
import logic.world.Company

class PassengerCarriage
(override val company : Company,
 _maxSpeed : Double,
 _weight : Double,
 var maxCapacity : Int)
extends Carriage(PASSENGER_CARRIAGE, company, _maxSpeed, _weight) {

  var nbPassenger = 0

  def loadPassenger(newPassenger : Int) : Int = {
    nbPassenger = math.max(newPassenger, maxCapacity)
    nbPassenger
  }

  def unloadPassenger() : Int = {
    val tempPassenger = nbPassenger
    nbPassenger = 0
    tempPassenger
  }

  private def evolve(newMaxSpeed : Double,
                     newWeight : Double,
                     newMaxCapacity : Int) : Unit = {
    super.evolve(newMaxSpeed, newWeight)

    maxCapacity = if (newMaxCapacity == NO_CHANGE) maxCapacity else newMaxCapacity
  }

}
