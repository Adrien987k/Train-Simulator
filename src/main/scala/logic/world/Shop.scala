package logic.world

import logic.items.ItemTypes._
import logic.items.transport.vehicules.components.VehicleComponentTypes._

object Shop {

  def price(item : ItemType, quantity : Int = 1) : Double = {
    item match {
      case DIESEL_TRAIN => 200
      case ELECTRIC_TRAIN => 300
      case BOEING => 800
      case CONCORDE => 2000
      case LINER => 1500
      case CRUISE_BOAT => 600
      case TRUCK => 75

      case STATION => 2000
      case AIRPORT => 5000
      case HARBOR => 4000
      case GAS_STATION => 1000

      case RAIL => 2
      case HIGHWAY => 1

      case _ => 0
    }
  }

  def fuelPrice(engineType : EngineType) : Double = {
    engineType match {
      case DIESEL_ENGINE => 20
      case ELECTRIC_ENGINE => 40
      case KEROSENE_ENGINE => 50
      case SHIP_ENGINE => 100

      case _ => 0
    }
  }

  def evolutionPrice(itemType : ItemType, level : Int) : Double = {
    itemType match {
      case _ : EngineType => level * 100

      case _ : PassengerCarriageType => level * 50

      case AIRPORT => level * 1000
      case STATION => level * 400
      case HARBOR => level * 750
      case GAS_STATION => level * 250

      case _ => 0
    }
  }

}
