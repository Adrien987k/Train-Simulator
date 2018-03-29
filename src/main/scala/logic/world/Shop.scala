package logic.world

import logic.items.ItemTypes._

object Shop {

  def price(item : ItemType, quantity : Int = 1): Int = {
    item match {
      case DIESEL_TRAIN => 200
      case ELECTRIC_TRAIN => 300
      case BOEING => 800
      case CONCORDE => 2000
      case STATION => 2000
      case AIRPORT => 5000
      case RAIL => 1
    }
  }

}
