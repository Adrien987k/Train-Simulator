package logic.items.transport.vehicules.components

import logic.exceptions.AlreadyMaxLevelException
import logic.items.EvolutionPlan
import logic.items.transport.vehicules.components.TrainComponentTypes.EngineType
import logic.world.Company

import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

case class EngineEvolutionPlan
(maxSpeeds : List[Double],
 weights : List[Double],
 maxTractiveEfforts : List[Double],
 maxFuelLevels : List[Double])
  extends EvolutionPlan(
    List(maxSpeeds, weights, maxTractiveEfforts, maxFuelLevels),
    15.0, 10.0
  )

abstract class Engine
(val engineType : EngineType,
 override val company : Company,
 override val evolutionPlan : EngineEvolutionPlan)
  extends TrainComponent(engineType, company, evolutionPlan) {

  private var _maxTractiveEffort : Double = evolutionPlan.level(level)(2)
  private var _maxFuelLevel : Double = evolutionPlan.level(level)(3)

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

  override def evolve() : Unit = {
    super.evolve()

    _maxTractiveEffort = evolutionPlan.level(level)(2)
    _maxFuelLevel = evolutionPlan.level(level)(3)

    company.buy(evolvePrice)
  }

  override def step() : Boolean = { false }

  val panel = new VBox()

  val engineLabel = new Label("=== " + engineType.name + " ===")
  val levelLabel = new Label()
  val fuelLevelLabel = new Label()

  val evolveButton = new Button("Evolve " + evolvePrice + "$")
  evolveButton.font = Font.font(null, FontWeight.ExtraBold, 19)
  evolveButton.textFill = Color.Green

  val warningLabel = new Label()

  evolveButton.onAction = _ => {
    try {
      evolve()
    } catch {
      case e : AlreadyMaxLevelException =>
        if (!levelIsMax) {
          warningLabel.text = e.getMessage
          warningLabel.font = Font.font(null, FontWeight.Bold, 18)

          if (panel.children.contains(evolveButton))
            panel.children.remove(evolveButton)
        }
    }
  }

  labels = List(engineLabel, levelLabel, fuelLevelLabel)

  panel.children = labels ++ List(evolveButton, warningLabel)

  styleLabels()

  override def propertyPane() : Node = {
    levelLabel.text = "Level : " + level
    fuelLevelLabel.text = "Fuel level : " + fuelLevel
    evolveButton.text = "Evolve " + evolvePrice + "$"

    panel
  }

}
