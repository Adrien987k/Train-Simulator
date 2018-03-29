package logic.world

import logic.items.ItemTypes._

object Shop {

  def price(item : ItemType, quantity : Int = 1): Int = {
    item match {
      case DIESEL_TRAIN => 200
      case ELECTRIC_TRAIN => 300
      case STATION => 1000
      case AIRPORT => 3000
      case RAIL => 1
    }
  }

}
