package logic.items.transport.vehicules

import logic.economy.BoxedResourcePack
import logic.items.ItemTypes.TruckType
import logic.items.transport.vehicules.components.{Engine, ResourceCarriage}
import logic.world.Company

import scala.collection.mutable.ListBuffer

abstract class Truck
(val truckType : TruckType,
 override val company : Company,
 override val engine : Engine,
 val carriage : ResourceCarriage[BoxedResourcePack], //TODO
 val initialGasStation : GasStation)
  extends Vehicle(truckType, company, engine, ListBuffer(carriage), Some(initialGasStation)) {

  def enterHighway(haigway : Highway) : Unit = {
    super.putOnRoad(highway)
  }

  def leaveHighway() : Unit = {
    super.removeFromRoad()
  }

}
