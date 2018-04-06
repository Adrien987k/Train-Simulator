package logic.items.transport.vehicules

import logic.items.ItemTypes.ShipType
import logic.items.transport.facilities.Harbor
import logic.items.transport.roads.Waterway
import logic.items.transport.vehicules.components.{Carriage, Engine}
import logic.world.Company

import scala.collection.mutable.ListBuffer

abstract class Ship
(val shipType : ShipType,
 override val company : Company,
 override val engine : Engine,
 override val carriages : ListBuffer[Carriage],
 val initialHarbor : Harbor)
  extends Vehicle(shipType, company, engine, carriages, Some(initialHarbor))
{

  def enterWaterway(waterway : Waterway) : Unit = {
    super.enterRoad(waterway)
  }

  def leaveWaterway() : Unit = {
    super.leaveRoad()
  }

}
