package logic.items.transport.vehicules.components

import logic.items.transport.vehicules.components.VehicleComponentTypes.CarriageType
import logic.world.Company

import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

abstract class Carriage
(val carriageType : CarriageType,
 override val company : Company,
 _maxSpeed : Double,
 _weight : Double)
  extends VehicleComponent(carriageType, company, _maxSpeed, _weight) {

  override def step() : Boolean = { false }

  override def propertyPane() : Node = { new BorderPane }

}
