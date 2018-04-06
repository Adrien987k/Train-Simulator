package logic.items.transport.vehicules

import logic.items.ItemTypes.TrainType
import logic.items.transport.facilities.Station
import logic.items.transport.roads.Rail
import logic.items.transport.vehicules.components.{Carriage, Engine}
import logic.world.Company

import scala.collection.mutable.ListBuffer

abstract class Train
(val trainType : TrainType,
 override val company: Company,
 override val engine : Engine,
 override val carriages : ListBuffer[Carriage],
 val initialStation : Station)
  extends Vehicle(trainType, company, engine, carriages, Some(initialStation)) {

  def putOnRail(rail : Rail) : Unit = {
    super.enterRoad(rail)
  }

  def removeFromRail() : Unit = {
    super.leaveRoad()
  }

}
