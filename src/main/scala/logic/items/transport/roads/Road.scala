package logic.items.transport.roads

import logic.{LineUpdatable, Loadable}
import logic.items.{EvolutionPlan, Item}
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.roads.RoadTypes.RoadType
import logic.items.transport.vehicules.Vehicle
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

case class RoadEvolutionPlan
(speedLimits : List[Double],
 capacities : List[Double],
 override val basePrice : Double,
 override val pricePerLevel : Double)
  extends EvolutionPlan(
    List(speedLimits, capacities), basePrice, pricePerLevel)

abstract class Road
(val roadType : RoadType,
 override val company : Company,
 val transportFacilityA : TransportFacility,
 val transportFacilityB : TransportFacility,
 override val evolutionPlan : RoadEvolutionPlan)
  extends Item(roadType, company, evolutionPlan) with LineUpdatable with Loadable{

  posA = transportFacilityA.pos
  posB = transportFacilityB.pos

  private var _speedLimit : Double = evolutionPlan.level(level).head
  private var _capacity : Int = evolutionPlan.level(level)(1).toInt

  private val vehicles : ListBuffer[Vehicle] = ListBuffer.empty

  private val _length : Double = transportFacilityA.pos.dist(transportFacilityB.pos)

  def speedLimit : Double = _speedLimit
  def capacity : Int = _capacity
  def length : Double = _length

  override def load(node: xml.Node): Unit = {

  }

  override def save: xml.Node = {
    roadType.name match {
      case "Rail" => <Rail
        townA={transportFacilityA.town.name}
        townB={transportFacilityB.town.name}
        />

      case "Line" => <Line
        townA={transportFacilityA.town.name}
        townB={transportFacilityB.town.name}
        />

      case "Waterway" => <Waterway
        townA={transportFacilityA.town.name}
        townB={transportFacilityB.town.name}
        />

      case "Highway" => <Highway
        townA={transportFacilityA.town.name}
        townB={transportFacilityB.town.name}
        />
    }
  }

  override def step() : Boolean = {
    vehicles.foreach(vehicle => {
      if (vehicle.crashed) {
        vehicle.leaveRoad()
        vehicles -= vehicle

        stats.newEvent(vehicle.vehicleType.name +  " crashed", 1)

        company.destroyVehicle(vehicle)
      }
    })

    false
  }

  def nbVehicle : Int = vehicles.size
  def isFull : Boolean = capacity == vehicles.size

  def addVehicle(vehicle : Vehicle) : Unit = {
    vehicles += vehicle

    stats.newEvent(vehicle.vehicleType.name + " entered")
  }

  def removeVehicle(vehicle: Vehicle) : Unit = {
    vehicles -= vehicle

    stats.newEvent(vehicle.vehicleType.name + " leaved")
  }

  override def evolve() : Unit = {
    super.evolve()

    _speedLimit = evolutionPlan.level(level).head
    _capacity = evolutionPlan.level(level)(1).toInt

    company.buy(evolvePrice, length.toInt)
  }

  val roadLabel = new Label("=== " + roadType.name + " ===")
  val maxCapLabel = new Label("Max capacity : " + capacity)
  val nbPlaneLabel = new Label()
  val lengthLabel = new Label()
  val connectLabel = new Label()

  labels = List(roadLabel,
    maxCapLabel, nbPlaneLabel, lengthLabel, connectLabel)

  panel.children = labels ++ List(evolveButton, statsButton)

  styleLabels()

  override def propertyPane() : Node = {

    maxCapLabel.text = "Max capacity : " + capacity
    nbPlaneLabel.text = "Vehicles : " + nbVehicle
    lengthLabel.text = "Length : " + length.toInt
    connectLabel.text = "Endpoints : " + transportFacilityA.town.name + ", " +
                                         transportFacilityB.town.name

    panel
  }

}
