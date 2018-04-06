package logic.items.transport.vehicules

import logic.world.Company
import logic.items.ItemTypes.PlaneType
import logic.items.transport.facilities.Airport
import logic.items.transport.vehicules.components.{Carriage, Engine}

import scala.collection.mutable.ListBuffer

class BasicPlane
(planeType : PlaneType,
 company : Company,
 initialAirport  : Airport,
 engine : Engine,
 carriages : ListBuffer[Carriage])
extends Plane(planeType, company, engine, carriages, initialAirport) {

}
