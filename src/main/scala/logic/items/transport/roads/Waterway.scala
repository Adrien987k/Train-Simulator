package logic.items.transport.roads

import logic.Updatable
import logic.items.ItemTypes.RoadType
import logic.items.transport.vehicules.Ship
import logic.world.Company

class Waterway
(override val roadType : RoadType,
 override val company: Company,
 val harborA : Harbor,
 val harborB : Harbor,
 override val speedLimit : Double)
  extends Road(roadType, company, harborA, harborB, speedLimit) with Updatable {

  def nbShips : Int = super.nbVehicle

  def addShip(ship : Ship) : Unit = super.addVehicle(ship)

  def removeShip(ship : Ship) : Unit = super.removeVehicle(ship)

}
