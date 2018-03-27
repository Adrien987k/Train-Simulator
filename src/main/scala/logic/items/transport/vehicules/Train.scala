package logic.items.transport.vehicules

import logic.items.transport.facilities.Station
import logic.items.transport.roads.Rail
import logic.world.Company

import scala.collection.mutable.ListBuffer

abstract class Train
(override val company: Company,
 override val engine : Engine,
 override val carriages : ListBuffer[Carriage],
 val initialStation : Station)
  extends Vehicle(company, engine, carriages, Some(initialStation)) {

  /*
  def setObjective(station : Station) : Unit = {
    super.setObjective(station)
  }*/

  def putOnRail(rail : Rail) : Unit = {
    super.putOnRoad(rail)
  }

  def removeFromRail() : Unit = {
    super.removeFromRoad()
  }

}
