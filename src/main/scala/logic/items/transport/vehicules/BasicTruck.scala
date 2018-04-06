package logic.items.transport.vehicules

import logic.economy.ResourcesTypes.BoxedResourceType
import logic.items.ItemTypes.TruckType
import logic.items.transport.facilities.GasStation
import logic.items.transport.vehicules.components.{Engine, ResourceCarriage}
import logic.world.Company

class BasicTruck
(truckType : TruckType,
 company : Company,
 initialGasStation : GasStation,
 engine : Engine,
 carriage : ResourceCarriage[BoxedResourceType])
  extends Truck(truckType, company, engine, carriage, initialGasStation) {

}
