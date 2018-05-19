package logic.items

import interface.GlobalInformationPanel
import logic.items.ItemTypes.ItemType
import logic.world.Company

abstract class Item
(val itemType : ItemType,
 val company : Company,
 val evolutionPlan : EvolutionPlan) {

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
  }

}
