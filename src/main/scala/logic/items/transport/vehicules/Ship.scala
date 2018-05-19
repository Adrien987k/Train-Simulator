package logic.items.transport.vehicules

import logic.economy.{Cargo, ResourceMap}
import logic.items.EvolutionPlan
import logic.items.transport.facilities.Harbor
import logic.items.transport.vehicules.VehicleTypes.ShipType
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node

case class ShipEvolutionPlan
(weights : List[Double],
 maxSpeedVars : List[Double],
 maxTractiveEfforts : List[Double],
 passengerCapacityVars : List[Double],
 maxFuelLevels : List[Double],
 cargoCapacities : List[Double])
  extends EvolutionPlan(
    List(weights, maxSpeedVars, maxTractiveEfforts,
      passengerCapacityVars, maxFuelLevels, cargoCapacities),
      200.0, 100.0)

class Ship
(val shipType : ShipType,
 override val company : Company,
 val initialHarbor : Harbor,
 override val evolutionPlan : ShipEvolutionPlan)
  extends Vehicle(shipType, company, Some(initialHarbor), evolutionPlan) {

  private var weight : Double = evolutionPlan.level(level).head
  private var maxSpeedVar : Double = evolutionPlan.level(level)(1)
  private var maxTractiveEffort : Double = evolutionPlan.level(level)(2)
  private var passengerCapacityVar : Int = evolutionPlan.level(level)(3).toInt
  private var maxFuelLevel : Double = evolutionPlan.level(level)(4)
  private var cargoCapacity : Int = evolutionPlan.level(level)(5).toInt

  private var _nbPassenger : Int = 0
  private var _fuelLevel : Double = 0

  private var cargoes : ListBuffer[Cargo] = ListBuffer.empty

  override def canTransportResource : Boolean = true

  override def maxSpeed() : Double = maxSpeedVar

  override def currentSpeed() : Double = {
    if (maxTractiveEffort != 0)
      maxSpeed * (1 - (totalWeight / maxTractiveEffort))

    maxSpeedVar
  }

  override def totalWeight : Double = weight

  override def refillFuel() : Unit = {
    _fuelLevel = maxFuelLevel
  }

  override def consume() : Unit = {
    _fuelLevel -= totalWeight * 0.005
  }

  override def evolve() : Unit = {
    super.evolve()

    val values = evolutionPlan.level(level)

    weight = values.head
    maxSpeedVar = values(1)
    maxTractiveEffort = values(2)
    passengerCapacityVar = values(3).toInt
    maxFuelLevel = values(4)
    cargoCapacity = values(5).toInt
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

  override def loadCargoes(newCargoes : ListBuffer[Cargo]) : Unit = {
    val toLoad = cargoCapacity - cargoes.size

    cargoes ++= newCargoes.take(toLoad)
  }

  override def unloadCargoes() : ListBuffer[Cargo] = {
    val result = cargoes

    cargoes = ListBuffer.empty

    result
  }

  override def carriagesPropertyPane() : Node = {
    val resourceMap = new ResourceMap

    cargoes.foldLeft(resourceMap)((map, cargo) => {
      cargo.resourceMap().merge(map)
    }).propertyPane()
  }

}
