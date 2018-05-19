package logic.items.transport.vehicules

import logic.economy.Cargo
import logic.items.EvolutionPlan
import logic.items.transport.facilities.Airport
import logic.items.transport.vehicules.VehicleTypes.PlaneType
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label

case class PlaneEvolutionPlan
(weights : List[Double],
 cruisingSpeeds : List[Double],
 maxSpeedVars : List[Double],
 passengerCapacityVars : List[Double],
 maxFuelLevels : List[Double])
  extends EvolutionPlan(
    List(weights, cruisingSpeeds, maxSpeedVars,
      passengerCapacityVars, maxFuelLevels), 500.0, 250.0)

class Plane
(val planeType : PlaneType,
 override val company : Company,
 val initialAirport : Airport,
 override val evolutionPlan : PlaneEvolutionPlan)
  extends Vehicle(planeType, company, Some(initialAirport), evolutionPlan) {

  private var weight : Double = evolutionPlan.level(level).head
  private var cruisingSpeed : Double = evolutionPlan.level(level)(1)
  private var maxSpeedVar : Double = evolutionPlan.level(level)(2)
  private var passengerCapacityVar : Int = evolutionPlan.level(level)(3).toInt
  private var maxFuelLevel : Double = evolutionPlan.level(level)(4)

  private var _nbPassenger : Int = 0
  private var _fuelLevel : Double = 0

  override def canTransportResource : Boolean = false

  override def maxSpeed() : Double = maxSpeedVar

  override def currentSpeed() : Double = {
    //TODO cruisingSpeed during travel
    maxSpeedVar
  }

  override def totalWeight : Double = {
    weight
  }

  override def refillFuel() : Unit = {
    _fuelLevel = maxFuelLevel
  }

  override def consume() : Unit = {
    val current = currentSpeed()

    val consumption : Double =
      if (totalWeight < weight * 1.5) current / 6.0
      else if (totalWeight < weight * 2.0) current / 4.5
      else if (totalWeight < weight * 3.0) current / 3.0
      else current / 1.5

    _fuelLevel -= weight * 0.5 * consumption
  }

  override def evolve() : Unit = {
    super.evolve()

    val values = evolutionPlan.level(level)

    weight = values.head
    cruisingSpeed  = values(1)
    maxSpeedVar = values(2)
    passengerCapacityVar = values(3).toInt
    maxFuelLevel  = values(4)
  }

  override def nbPassenger() : Int = _nbPassenger

  override def passengerCapacity() : Int = passengerCapacityVar

  override def loadPassenger(newPassengers : Int) : Int = {
    val canLoad =
      if (newPassengers + _nbPassenger > passengerCapacity)
        passengerCapacity - _nbPassenger
      else
        newPassengers

    _nbPassenger += canLoad
    canLoad
  }

  override def unloadPassenger() : Int = {
    val passenger = _nbPassenger
    _nbPassenger = 0
    passenger
  }

  /**
    * Planes cannot transport resources
    */
  override def loadCargoes(packs : ListBuffer[Cargo]) : Unit = { }
  override def unloadCargoes(): ListBuffer[Cargo] = ListBuffer.empty

  override def carriagesPropertyPane() : Node = {
    new Label("=== resources ===\n")
  }
}
