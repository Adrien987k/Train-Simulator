package logic.items.transport.vehicules

import logic.items.transport.facilities.GasStation
import logic.items.transport.roads.Highway
import logic.items.transport.vehicules.VehicleTypes.TruckType
import logic.items.transport.vehicules.components.{Engine, ResourceCarriage}
import logic.world.Company

import scala.collection.mutable.ListBuffer

class Truck
(val truckType : TruckType,
 override val company : Company,
 override val engine : Engine,
 val carriage : ResourceCarriage,
 val initialGasStation : GasStation)
  extends Vehicle(truckType, company, engine, ListBuffer(carriage), Some(initialGasStation)) {

  def enterHighway(highway : Highway) : Unit = {
    super.enterRoad(highway)
  }

  def leaveHighway() : Unit = {
    super.leaveRoad()
  }

  override def canTransportResource: Boolean = true

}
