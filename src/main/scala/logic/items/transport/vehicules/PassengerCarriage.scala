package logic.items.transport.vehicules

class PassengerCarriage
(val maxSpeed : Double,
 val maxWeight : Double,
 val maxCapacity : Int)
extends Carriage(maxSpeed, maxWeight) {

  var nbPassenger = 0

}
