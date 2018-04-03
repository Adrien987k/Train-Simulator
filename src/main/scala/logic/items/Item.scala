package logic.items

import logic.items.ItemTypes.ItemType
import logic.world.Company

abstract class Item(val itemType : ItemType, val company : Company) {

  val NO_CHANGE : Double = -1

  private var _level = 1

  private var _levelIsMax = false

  def level : Int = _level
  def level_= (value : Int) : Unit = _level = value

  def levelIsMax : Boolean = _levelIsMax

  def maxLevelReached() : Unit = _levelIsMax = true

  def evolve()

}
