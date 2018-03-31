package logic.items.transport.vehicules

import logic.items.transport.vehicules.VehicleComponentType.EngineType

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

abstract class Engine
(val engineType : EngineType,
 val maxSpeed : Double,
 val maxWeight : Double,
 val maxTractiveEffort : Double,
 val maxFuelLevel : Double)
  extends VehicleComponent(maxSpeed, maxWeight) {

  private var _fuelLevel : Double = maxFuelLevel

  def fuelLevel : Double = _fuelLevel

  def refillFuelLevel() : Unit = {
    _fuelLevel = maxFuelLevel
  }

  def consume(weight : Double) : Unit = {
    _fuelLevel -= consumption(weight)
  }


  def tractiveEffort(weight : Double) : Double

  def consumption(weight : Double) : Double

  override def step(): Boolean = { false }

  val panel = new VBox()

  val engineLabel = new Label("=== Engine ===")
  val fuelLevelLabel = new Label()

  labels = List(engineLabel, fuelLevelLabel)

  panel.children = labels

  styleLabels()

  override def propertyPane() : Node = {
    fuelLevelLabel.text = "Fuel level : " + fuelLevel

    panel
  }

}
