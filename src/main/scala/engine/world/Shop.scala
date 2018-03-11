package engine.world

import engine.items.ItemType
object Shop {

  val RAIL_PRICE_PER_KM = 1
  val STATION_PRICE = 500
  val TRAIN_PRICE = 200

  def price(item : ItemType.Value, quantity : Int = 1): Int = {
    item match {
      case ItemType.TRAIN => TRAIN_PRICE
      case ItemType.STATION => STATION_PRICE
      case ItemType.RAIL => quantity * RAIL_PRICE_PER_KM
    }
  }

}
