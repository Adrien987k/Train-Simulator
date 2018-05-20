package logic.items

import interface.GlobalInformationPanel
import logic.items.ItemTypes.ItemType
import logic.world.Company
import statistics.Statistics

import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

abstract class Item
(val itemType : ItemType,
 val company : Company,
 val evolutionPlan : EvolutionPlan) {

  protected val stats : Statistics = new Statistics(itemType.name)

  private var _level = 1

  private var _levelIsMax = false

  def level : Int = _level
  def level_= (value : Int) : Unit = _level = value

  def levelIsMax : Boolean = _levelIsMax

  def maxLevelReached() : Unit = _levelIsMax = true

  protected def evolvePrice : Double =
    evolutionPlan.basePrice + (level * evolutionPlan.pricePerLevel)

  protected def buyEvolution() : Unit = {
    val price = evolutionPlan.basePrice + ((level - 1) * evolutionPlan.pricePerLevel)

    company.buy(price)
  }

  def evolve() : Unit =  {
    if (evolutionPlan.isMaxLevel(level)) return

    if (!company.canBuy(evolvePrice)) {
      GlobalInformationPanel.displayWarningMessage("Not enough money to evolve" + itemType.name)

      return
    }

    _level += 1

    stats.newEvent("Evolved to level", level)
  }

  protected val panel = new VBox

  protected val evolveButton = new Button
  protected val statsButton = new Button

  evolveButton.text = "Evolve $" + evolvePrice
  evolveButton.font = Font.font(null, FontWeight.Bold, 18)
  evolveButton.textFill = Color.Green
  evolveButton.onAction = _ => {
    evolve()

    if (evolutionPlan.isMaxLevel(level)) {
      panel.children.remove(evolveButton)
    }
  }

  statsButton.text = "Statistics"
  statsButton.font = Font.font(null, FontWeight.Bold, 18)
  statsButton.onAction = _ => {
    stats.show()
  }

}
