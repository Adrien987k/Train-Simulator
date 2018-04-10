package logic.items.transport.roads

import logic.LineUpdatable
import logic.items.Item
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.roads.RoadTypes.RoadType
import logic.items.transport.vehicules.Vehicle
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

abstract class Road
(val roadType : RoadType,
 override val company : Company,
 val transportFacilityA : TransportFacility,
 val transportFacilityB : TransportFacility,
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
        vehicle.leaveRoad()
        vehicles -= vehicle

        company.destroyVehicle(vehicle)
      }
    })

    false
  }

  def nbVehicle : Int = vehicles.size
  def isFull : Boolean = capacity == vehicles.size

  def addVehicle(vehicle : Vehicle) : Unit = vehicles += vehicle
  def removeVehicle(vehicle: Vehicle) : Unit = vehicles -= vehicle

  override def evolve() : Unit = {
    //TODO evolve rail
  }

  val panel = new VBox()

  val roadLabel = new Label("=== " + roadType.name + " ===")
  val maxCapLabel = new Label("Max capacity : " + DEFAULT_CAPACITY)
  val nbPlaneLabel = new Label()
  val lengthLabel = new Label()
  val connectLabel = new Label()

  labels = List(roadLabel, maxCapLabel, nbPlaneLabel, lengthLabel, connectLabel)

  panel.children = labels

  styleLabels()

  override def propertyPane() : Node = {

    maxCapLabel.text = "Max capacity : " + DEFAULT_CAPACITY
    nbPlaneLabel.text = "Vehicles : " + nbVehicle
    lengthLabel.text = "Length : " + length.toInt
    connectLabel.text = "Endpoints : " + transportFacilityA.town.name + ", " +
                                         transportFacilityB.town.name

    panel
  }

}
