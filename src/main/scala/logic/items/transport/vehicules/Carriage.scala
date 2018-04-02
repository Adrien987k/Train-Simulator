package logic.items.transport.vehicules
import logic.items.transport.vehicules.VehicleComponentTypes.CarriageType

import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

abstract class Carriage
(val carriageType : CarriageType,
 maxSpeed : Double,
 maxWeight : Double)
  extends VehicleComponent(carriageType, maxSpeed, maxWeight) {

  override def step() : Boolean = { false }

  override def propertyPane() : Node = { new BorderPane }

}
