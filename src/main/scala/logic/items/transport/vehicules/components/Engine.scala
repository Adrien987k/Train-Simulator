package logic.items.transport.vehicules.components

import logic.exceptions.AlreadyMaxLevelException
import logic.items.transport.vehicules.components.VehicleComponentTypes.EngineType
import logic.world.Company

import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

abstract class Engine
(val engineType : EngineType,
 override val company : Company,
 _maxSpeed : Double,
 _weight : Double,
 private var _maxTractiveEffort : Double,
 private var _maxFuelLevel : Double)
  extends VehicleComponent(engineType, company, _maxSpeed, _weight) {

  private var _fuelLevel : Double = maxFuelLevel

  def maxTractiveEffort : Double = _maxTractiveEffort
  def maxFuelLevel : Double = _maxFuelLevel

  def fuelLevel : Double = _fuelLevel

  def refillFuelLevel() : Unit = {
    _fuelLevel = maxFuelLevel
  }

  def consume(weight : Double) : Unit = {
    _fuelLevel -= consumption(weight)
  }

  def tractiveEffort(weight : Double) : Double

  def consumption(weight : Double) : Double

  def evolve(newMaxSpeed : Double,
             newMaxWeight : Double,
             newMaxTractiveEffort : Double,
             newMaxFuelLevel : Double) : Unit = {
    super.evolve(newMaxSpeed, newMaxWeight)

    _maxFuelLevel = if (newMaxFuelLevel == NO_CHANGE) maxFuelLevel else newMaxFuelLevel
    _maxTractiveEffort = if (newMaxTractiveEffort == NO_CHANGE) maxTractiveEffort else newMaxTractiveEffort
  }

  override def step(): Boolean = { false }

  val panel = new VBox()

  val engineLabel = new Label("=== Engine ===")
  val fuelLevelLabel = new Label()

  val evolveButton = new Button("Evolve")
  evolveButton.font = Font.font(null, FontWeight.ExtraBold, 19)
  evolveButton.textFill = Color.Green

  val warningLabel = new Label()

  evolveButton.onAction = _ => {
    try {
      evolve()
    } catch {
      case e : AlreadyMaxLevelException =>
        warningLabel.text = e.getMessage
    }
  }

  labels = List(engineLabel, fuelLevelLabel)

  panel.children = labels ++ List(evolveButton, warningLabel)

  styleLabels()

  override def propertyPane() : Node = {
    fuelLevelLabel.text = "Fuel level : " + fuelLevel

    panel
  }

}
