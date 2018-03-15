package logic.items.transport.vehicules

class PassengerCarriage
(val maxSpeed : Double,
 val maxWeight : Double,
 val maxCapacity : Int)
extends Carriage(maxSpeed, maxWeight) {

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

}
