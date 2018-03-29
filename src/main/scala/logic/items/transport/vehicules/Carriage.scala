package logic.items.transport.vehicules

abstract class Carriage
(val maxSpeed : Double,
 val maxWeight : Double)
  extends VehicleComponent(maxSpeed, maxWeight) {

}
