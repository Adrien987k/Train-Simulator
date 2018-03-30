package logic.items.transport.vehicules

import logic.exceptions.ImpossibleActionException
import logic.{PointUpdatable, UpdateRate}
import logic.items.Item
import logic.items.ItemTypes.VehicleType
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.roads.Road
import logic.world.Company
import logic.world.towns.Town
import utils.{Dir, Pos}

import scala.collection.mutable.ListBuffer

abstract class Vehicle
(val vehicleType : VehicleType,
 override val company : Company,
 val engine : Engine,
 val carriages : ListBuffer[Carriage],
 var currentTransportFacility : Option[TransportFacility])
extends Item(vehicleType, company) with PointUpdatable {

  updateRate(UpdateRate.TRAIN_UPDATE)

  pos = currentTransportFacility match {
    case Some(tf) => tf.pos.copy()
    case None => new Pos(0, 0)
  }

  var dir : Dir = new Dir(0, 0)

  /* The next facility to reach */
  var goalTransportFacility : Option[TransportFacility] = None

  /* The final town to reach */
  var destination : Option[TransportFacility] = None

  var currentRoad : Option[Road] = None

  var fuelLevel = 0

  override def step() : Boolean = {
    if(!super.step()) return false

    goalTransportFacility match {
      case Some(transportFacility) =>
        if (pos.inRange(transportFacility.pos, 10)) {

          if (destination.nonEmpty && transportFacility == destination.get)
            destination = None

          removeFromRoad()
          transportFacility.unload(this)
        } else {
          val speed = currentSpeed()
          pos.x += dir.x * speed
          pos.y += dir.y * speed

          //TODO consume()
        }
      case None =>
    }

    true
  }

  def currentSpeed() : Double = {
    var speed = engine.tractiveEffort(totalWeight)

    if (currentRoad.nonEmpty && speed > currentRoad.get.speedLimit)
      speed = currentRoad.get.speedLimit

    speed
  }

  def totalWeight : Double = {
    carriages.foldLeft(0.0)((total, carriage) =>
      total + carriage.maxWeight
    )
  }

  def consume() : Unit = {
    fuelLevel -= 10

    if (fuelLevel <= 0) crash()
  }

  def crash() : Unit = {

  }

  def setObjective(transportFacility : TransportFacility) : Unit = {
    if (goalTransportFacility.nonEmpty) return

    goalTransportFacility = Some(transportFacility)

    dir.x = transportFacility.pos.x - pos.x
    dir.y = transportFacility.pos.y - pos.y
    dir.normalize()
  }

  def unsetObjective() : Unit = {
    goalTransportFacility = None
  }

  def setDestination(town : Town) : Unit = {
    destination = town.transportFacilityForVehicleType(vehicleType)
  }

  def putOnRoad(road : Road) : Unit = {
    if (road.isFull) throw new ImpossibleActionException("Road is full")
    currentRoad match {
      case Some(_) =>
        throw new ImpossibleActionException("Train already in a road")
      case None =>
        currentTransportFacility = None
        road.addVehicle(this)
        currentRoad = Some(road)
    }
  }

  def removeFromRoad() : Unit = {
    currentRoad match {
      case Some(road) =>
        road.removeVehicle(this)
        currentRoad = None
      case None =>
    }
  }

  def addCarriage(carriage: Carriage) : Unit = {
    carriages += carriage
  }

  /**
    * @param nbPassenger The number of passenger to load
    * @return The number of loaded passenger
    */
  def loadPassenger(nbPassenger : Int) : Int = {
    var remainingPassenger = nbPassenger
    carriages.foreach {
      case passengerCarriage : PassengerCarriage =>
        if (remainingPassenger > 0) {
          remainingPassenger -=
            passengerCarriage.loadPassenger(remainingPassenger)
        }
      case _ =>
    }
    nbPassenger - remainingPassenger
  }

  /**
    * Empty the train of its passengers
    *
    * @return The number of passenger in the train
    */
  def unloadPassenger() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.unloadPassenger()
        case _ => total
      }
    })
  }

  /**
    * @return The total number of passenger in the train
    */
  def nbPassenger() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.nbPassenger
        case _ => total
      }
    })
  }

  /**
    * @return The total passenger capacity of this train
    */
  def passengerCapacity() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.maxCapacity
        case _ => total
      }
    })
  }

}
