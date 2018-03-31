package logic.items.transport.roads

import logic.items.Item
import logic.items.ItemTypes.RoadType
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.vehicules.Vehicle
import logic.world.{Company, LineUpdatable}

import scala.collection.mutable.ListBuffer

abstract class Road
(val roadType: RoadType,
 override val company : Company,
 val transportFacilityA: TransportFacility,
 val transportFacilityB: TransportFacility,
 val speedLimit : Double)
  extends Item(roadType, company) with LineUpdatable {

  posA = transportFacilityA.pos
  posB = transportFacilityB.pos

  val DEFAULT_CAPACITY = 3

  val capacity : Int = DEFAULT_CAPACITY

  val vehicles : ListBuffer[Vehicle] = ListBuffer.empty

  val length : Double = transportFacilityA.pos.dist(transportFacilityB.pos)

  override def step() : Boolean = {
    vehicles.foreach(vehicle => {
      if (vehicle.crashed) {
        vehicle.removeFromRoad()
        vehicles -= vehicle

        println("ROAD CRASHED")
        company.destroyVehicle(vehicle)
      }
    })

    false
  }

  def nbVehicle : Int = vehicles.size
  def isFull : Boolean = capacity == vehicles.size

  def addVehicle(vehicle : Vehicle) : Unit = vehicles += vehicle
  def removeVehicle(vehicle: Vehicle) : Unit = vehicles -= vehicle

}
