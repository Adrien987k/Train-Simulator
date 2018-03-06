package engine

import engine.ItemType.{RAIL, STATION, TRAIN}

object Shop {

  val RAIL_PRICE_PER_KM = 1
  val STATION_PRICE = 500
  val TRAIN_PRICE = 200

  def price(item : ItemType.Value, quantity : Int = 1): Int = {
    item match {
      case TRAIN => TRAIN_PRICE
      case STATION => STATION_PRICE
      case RAIL => quantity * RAIL_PRICE_PER_KM
    }
  }

}
