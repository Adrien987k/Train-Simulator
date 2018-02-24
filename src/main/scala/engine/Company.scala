package engine

import engine.ItemType.Value

class Company {

  var money = 0
  private var selected:Option[ItemType.Value] = None

  def select(itemType: ItemType.Value): Unit = {
    selected = Some(itemType)
  }

  def buy(itemType: ItemType.Value): Boolean = {
    val price = Shop.price(itemType)
    if (money - price < 0) return false
    else {
      money -= price
    }
    //val item = ItemFactory.buildItem(itemType)
    //item.place()
    //Add item to World
    true
  }
}
