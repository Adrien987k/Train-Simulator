package logic.items.transport.vehicules

import logic.items.transport.facilities.Airport
import logic.items.transport.roads.Line
import logic.items.transport.vehicules.VehicleTypes.PlaneType
import logic.items.transport.vehicules.components.{Carriage, Engine}
import logic.world.Company

import scala.collection.mutable.ListBuffer

class Plane
(val planeType : PlaneType,
 override val company : Company,
 override val engine : Engine,
 override val carriages : ListBuffer[Carriage],
 val initialAirport : Airport)
  extends Vehicle(planeType, company, engine, carriages, Some(initialAirport)) {

  def putOnLine(line : Line) : Unit = {
    super.enterRoad(line)
  }

  def removeFromRail() : Unit = {
    super.leaveRoad()
  }

  override def canTransportResource: Boolean = false

}
