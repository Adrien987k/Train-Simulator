package logic.items.transport.vehicules

import logic.items.ItemTypes.ShipType
import logic.items.transport.vehicules.components.{Carriage, Engine}
import logic.world.Company

import scala.collection.mutable.ListBuffer

class BasicShip
(shipType : ShipType,
 company : Company,
 initialHarbor : Harbor,
  engine : Engine,
  carriages : ListBuffer[Carriage])
  extends Ship(shipType, company, engine, carriages, initialHarbor) {

}
