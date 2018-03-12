package logic.items.transport.facilities

import logic.{Updatable, UpdateRate}
import logic.items.Item
import logic.items.transport.roads.Road
import logic.items.transport.vehicules.{BasicTrain, Vehicle}
import logic.world.Company
import logic.world.towns.Town
import utils.Pos

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

abstract class TransportFacility
(val company : Company,
 private var _pos : Pos,
 val town : Town)
  extends Item(company) with Updatable {

  updateRate_=(1)

  def pos: Pos = _pos
  def pos_= (value : Pos) : Unit = _pos = value
  def pos_= (x : Double, y : Double) : Unit = _pos = new Pos(x, y)

  val DEFAULT_CAPACITY = 5

  var capacity : Int = DEFAULT_CAPACITY

  var waitingPassengers : mutable.HashMap[TransportFacility, Int] = mutable.HashMap.empty

  private val vehicles : ListBuffer[Vehicle] = ListBuffer.empty
  private val roads : ListBuffer[Road] = ListBuffer.empty

  def addRoad(road : Road) : Unit = {
    roads += road
  }

  def addVehicle(vehicle : Vehicle) : Boolean = {
    if (isFull) return false
    //TODO Add vehicle to company
    //company.trains += vehicle
    vehicles += vehicle
    true
  }

  def isFull : Boolean = vehicles.lengthCompare(capacity) == 0
  def availableVehicles : Int = vehicles.size

  def nbWaitingPassengers(): Int = {
    waitingPassengers.foldLeft(0) {
      case (total, (_, nb)) => total + nb
    }
  }

  def neighbours() : ListBuffer[TransportFacility] = {
    val neighbourList : ListBuffer[TransportFacility] = ListBuffer.empty
    for (road <- roads) {
      if (road.transportFacilityA == this) neighbourList += road.transportFacilityB
      if (road.transportFacilityB == this) neighbourList += road.transportFacilityA
    }
    neighbourList
  }

  def nbNeighbours() : Int = roads.size

  override def step(): Unit = {
    super.step()
    for ((station, nbPassenger) <- waitingPassengers) {
      val success = sendPassenger(station, nbPassenger)
      if (success) waitingPassengers -= station
    }
  }

  def sendPassenger(objective : TransportFacility, nbPassenger : Int) : Boolean = {
    if (vehicles.isEmpty) {
      waitingPassengers += ((objective, nbPassenger))
      return false
    }
    getRoadTo(objective) match {
      case Some(road) =>
        if (road.nbVehicle == road.DEFAULT_CAPACITY) {
          waitingPassengers += ((objective, nbPassenger))
          return false
        }
        val vehicle = vehicles.remove(0)
        var realPassenger = 50
        var waiters = 0
        /*if (train.passengerCapacity < nbPassenger) {
          realPassenger = train.passengerCapacity
          waiters = nbPassenger - train.passengerCapacity
        } else {
          realPassenger = nbPassenger
        }*/
        //TODO Take into account passengers carriages
        if (waiters > 0) waitingPassengers += ((objective, waiters))
        load(vehicle, objective, realPassenger)
        val success = vehicle.putOnRoad(road)
        if (success) {
          company.money += nbPassenger * road.length * company.ticketPricePerKm
        } else {
          unload(vehicle)
          return false
        }
      case None =>
        waitingPassengers += ((objective, nbPassenger))
        return false
    }
    true
  }

  def getRoadTo(transportFacility: TransportFacility) : Option[Road] = {
    roads.find(road => road.transportFacilityA == transportFacility
      || road.transportFacilityB == transportFacility)
  }

  def load(vehicle : Vehicle, objective : TransportFacility, nbPassenger : Int) : Unit = {
    town.population -= nbPassenger
    vehicle.setObjective(objective, pos)
    //TODO Unload passenger carriage
    //vehicle.nbPassenger = nbPassenger
  }

  def unload(vehicle : Vehicle) : Unit = {
    //town.population += vehicle.nbPassenger
    vehicle.unsetObjective()
    //vehicle.nbPassenger = 0
    vehicles += vehicle
  }

}
