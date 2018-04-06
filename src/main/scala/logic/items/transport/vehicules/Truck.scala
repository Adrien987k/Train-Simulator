package logic.items.transport.vehicules

import logic.economy.ResourcesTypes.BoxedResourceType
import logic.items.ItemTypes.TruckType
import logic.items.transport.facilities.GasStation
import logic.items.transport.roads.Highway
import logic.items.transport.vehicules.components.{Engine, ResourceCarriage}
import logic.world.Company

import scala.collection.mutable.ListBuffer

abstract class Truck
(val truckType : TruckType,
 override val company : Company,
 override val engine : Engine,
 val carriage : ResourceCarriage[BoxedResourceType], //TODO
 val initialGasStation : GasStation)
  extends Vehicle(truckType, company, engine, ListBuffer(carriage), Some(initialGasStation)) {

  def enterHighway(highway : Highway) : Unit = {
    super.enterRoad(highway)
  }

  def leaveHighway() : Unit = {
    super.leaveRoad()
  }

}
