package logic.items.transport.roads

import logic.Updatable
import logic.items.Item
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.vehicules.Vehicle
import logic.world.Company
import utils.Pos

import scala.collection.mutable.ListBuffer

abstract class Road
(val company : Company,
 val transportFacilityA: TransportFacility,
 val transportFacilityB: TransportFacility)
  extends Item(company) with Updatable {

  val DEFAULT_CAPACITY = 3

  val capacity : Int = DEFAULT_CAPACITY

  val vehicles : ListBuffer[Vehicle] = ListBuffer.empty

  val length : Double = transportFacilityA.pos.dist(transportFacilityB.pos)

  override def pos: Pos = transportFacilityA.pos

  override def step(): Unit = {}

  def nbVehicle : Int = vehicles.size
  def isFull : Boolean = capacity == vehicles.size

  def addVehicle(vehicle : Vehicle) : Unit = vehicles += vehicle
  def removeVehicle(vehicle: Vehicle) : Unit = vehicles -= vehicle

}
