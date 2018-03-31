package logic.world

import logic.items.ItemTypes._
import logic.items.transport.vehicules.VehicleComponentType.{DIESEL_ENGINE, ELECTRIC_ENGINE, EngineType, KEROSENE_ENGINE}

object Shop {

  def price(item : ItemType, quantity : Int = 1) : Double = {
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

  def fuelPrice(engineType : EngineType) : Double = {
    engineType match {
      case DIESEL_ENGINE => 20
      case ELECTRIC_ENGINE => 40
      case KEROSENE_ENGINE => 50
    }
  }

}
