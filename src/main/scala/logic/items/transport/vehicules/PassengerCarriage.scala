package logic.items.transport.vehicules

import logic.items.transport.vehicules.VehicleComponentTypes.PASSENGER_CARRIAGE

class PassengerCarriage
(maxSpeed : Double,
 maxWeight : Double,
 var maxCapacity : Int)
extends Carriage(PASSENGER_CARRIAGE, maxSpeed, maxWeight) {

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
                      newMaxWeight : Double,
                      newMaxCapacity : Int) : Unit = {
    super.evolve(newMaxSpeed, newMaxWeight)

    maxCapacity = if (newMaxCapacity == NO_CHANGE) maxCapacity else newMaxCapacity
  }

}
