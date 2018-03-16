package logic.world

import logic.items.ItemTypes
import logic.items.ItemTypes.{ItemType, RoadType, TrainType, TransportFacilityType}
object Shop {

  val RAIL_PRICE_PER_KM = 1
  val STATION_PRICE = 500
  val TRAIN_PRICE = 200

  def price(item : ItemType, quantity : Int = 1): Int = {
    item match {
      case _ : TrainType => TRAIN_PRICE
      case _ : TransportFacilityType => STATION_PRICE
      case _ : RoadType => quantity * RAIL_PRICE_PER_KM
    }
  }

}
