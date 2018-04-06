package logic.items.transport.vehicules

import logic.economy.BoxedResourcePack
import logic.items.ItemTypes.TruckType
import logic.items.transport.vehicules.components.{Carriage, Engine, ResourceCarriage}
import logic.world.Company

class BasicTruck
(truckType : TruckType,
 company : Company,
 initialGasStation : GasStation,
 engine : Engine,
 carriage : ResourceCarriage[BoxedResourcePack])
  extends Truck(truckType, company, engine, carriage, initialGasStation) {

}
