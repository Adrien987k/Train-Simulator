package logic.items.transport.vehicules
import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

abstract class Carriage
(val maxSpeed : Double,
 val maxWeight : Double)
  extends VehicleComponent(maxSpeed, maxWeight) {

  override def step() : Boolean = { false }

  override def propertyPane() : Node = { new BorderPane }

}
