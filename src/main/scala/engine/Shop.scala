package engine

import engine.ItemType.{ROAD, STATION, TRAIN}

object Shop {
  val ROAD_PRICE = 100
  val STATION_PRICE = 500
  val TRAIN_PRICE = 200

  def price(item: ItemType.Value): Int = {
    item match {
      case TRAIN => TRAIN_PRICE
      case STATION => STATION_PRICE
      case ROAD => ROAD_PRICE
    }
  }
}
