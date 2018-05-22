package logic.items.transport.vehicules

import logic.economy.Cargo
import logic.items.EvolutionPlan
import logic.items.transport.facilities.GasStation
import logic.items.transport.roads.Highway
import logic.items.transport.vehicules.VehicleTypes.TruckType
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label

case class TruckEvolutionPlan
(maxSpeedVars : List[Double],
 weights : List[Double],
 maxFuelLevels : List[Double])
  extends EvolutionPlan(
    List(maxSpeedVars, weights, maxFuelLevels), 50.0, 50.0)

class Truck
(val truckType : TruckType,
 override val company : Company,
 val initialGasStation : GasStation,
 override val evolutionPlan : TruckEvolutionPlan)
  extends Vehicle(truckType, company, Some(initialGasStation), evolutionPlan) {

  private var maxSpeedVar : Double = evolutionPlan.level(level).head
  private var weight : Double = evolutionPlan.level(level)(1)
  private var maxFuelLevel : Double = evolutionPlan.level(level)(2)

  private var _fuelLevel : Double = 0.0

  private var cargoOpt : Option[Cargo] = None

  def haveCargo : Boolean = cargoOpt.nonEmpty

  def loadCargo(cargo : Cargo) : Unit = {
    cargoOpt = Some(cargo)
  }

  def unloadCargo() : Option[Cargo] = {
    cargoOpt match {
      case Some(cargo) =>
        cargoOpt = None
        Some(cargo)

      case None => None
    }
  }

  override def canTransportResource: Boolean = true

  override def maxSpeed() : Double = maxSpeedVar

  override def currentSpeed() : Double = maxSpeedVar

  override def totalWeight : Double = {
    cargoOpt match {
      case Some(cargo) => cargo.totalWeight
      case None => 0
    }
  }

  override def refillFuel() : Unit = {
    _fuelLevel = maxFuelLevel
  }

  override def consume() : Unit = {
    _fuelLevel -= 5
  }

  override def evolve() : Unit = {
    super.evolve()

    val values = evolutionPlan.level(level)

    maxSpeedVar = values.head
    weight = values(1)
    maxFuelLevel = values(2)
  }

  /**
    * Trucks cannot transport passengers
    */
  override def loadPassenger(nbPassenger : Int) : Int = 0
  override def unloadPassenger() : Int = 0
  override def nbPassenger(): Int = 0
  override def passengerCapacity() : Int = 0

  override def loadCargoes(cargoes : ListBuffer[Cargo]) : Unit = {
    cargoOpt = Some(cargoes.head)
  }

  override def unloadCargoes() : ListBuffer[Cargo] = {
    cargoOpt match {
      case Some(cargo) =>
        cargoOpt = None
        ListBuffer(cargo)

      case None =>
        ListBuffer.empty
    }
  }

  override def carriagesPropertyPane() : Node = {
    cargoOpt match {
      case Some(cargo) =>
        cargo.resourceMap().propertyPane()

      case None =>
        new Label("")
    }
  }

}
