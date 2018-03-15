package logic.items.transport.vehicules

import logic.exceptions.ImpossibleActionException
import logic.{PointUpdatable, UpdateRate}
import logic.items.Item
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.roads.Road
import logic.world.Company
import logic.world.towns.Town
import utils.{Dir, Pos}

import scala.collection.mutable.ListBuffer

abstract class Vehicle
(val company : Company,
 val engine : Engine,
 val carriages : ListBuffer[Carriage],
 var currentTransportFacility : Option[TransportFacility])
extends Item(company) with PointUpdatable {

  updateRate(UpdateRate.TRAIN_UPDATE)

  pos = currentTransportFacility match {
    case Some(tf) => tf.pos
    case None => new Pos(0, 0)
  }

  var dir : Dir = new Dir(0, 0)

  /* The next facility to reach */
  var goalTransportFacility : Option[TransportFacility] = None

  /* The final town to reach */
  var destination : Option[Town] = None

  var currentRoad : Option[Road] = None

  override def step(): Unit = {
    super.step()
    goalTransportFacility match {
      case Some(station) =>
        if (pos.inRange(station.pos, 10)) {
          removeFromRoad()
          station.unload(this)
        } else {
          pos.x += dir.x * engine.speed
          pos.y += dir.y * engine.speed
        }
      case None =>
    }
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
    destination = Some(town)
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

}
