package engine

import engine.ItemType.ItemType

class Company {

  var money = 0
  private var selected:Option[ItemType] = None

  def select(itemType: ItemType): Unit = {
    selected = Some(itemType)
  }

  def buy(itemType: ItemType): Boolean = {
    val price = Shop.price(itemType)
    if (money - price < 0) return false
    else {
      money -= price
    }
    val item = ItemFactory.buildItem(itemType)
    item.place()
    //Add item to World
    true
  }
}
