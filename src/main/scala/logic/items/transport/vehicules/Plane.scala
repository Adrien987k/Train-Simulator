package logic.items.transport.vehicules

import logic.items.ItemTypes.PlaneType
import logic.items.transport.facilities.Airport
import logic.items.transport.roads.Line
import logic.world.Company

import scala.collection.mutable.ListBuffer

abstract class Plane
(val planeType : PlaneType,
 override val company : Company,
 override val engine : Engine,
 override val carriages : ListBuffer[Carriage],
 val initialAirport : Airport)
  extends Vehicle(planeType, company, engine, carriages, Some(initialAirport)) {

  def putOnLine(line : Line) : Unit = {
    super.putOnRoad(line)
  }

  def removeFromRail() : Unit = {
    super.removeFromRoad()
  }

}
