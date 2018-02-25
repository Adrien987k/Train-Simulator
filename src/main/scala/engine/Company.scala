package engine

import engine.ItemType.Value
import utils.Pos

class Company {

  var money = 0
  private var selected:Option[ItemType.Value] = None

  def select(itemType: ItemType.Value): Unit = {
    selected = Some(itemType)
  }

  def place(itemType: ItemType.Value, pos : Pos): Boolean = {
    //val item = ItemFactory.buildItem(itemType)
    //item.place()
    //Add item to World
    true
  }

  def buy(itemType: ItemType.Value): Boolean = {
    val price = Shop.price(itemType)
    if (money - price < 0) return false
    else {
      money -= price
    }
    true
  }
}
