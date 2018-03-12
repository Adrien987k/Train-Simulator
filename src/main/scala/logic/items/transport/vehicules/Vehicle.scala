package logic.items.transport.vehicules

import logic.{Updatable, UpdateRate}
import logic.items.Item
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.roads.Road
import logic.world.Company
import logic.world.towns.Town
import utils.{Dir, Pos}

import scala.collection.mutable.ListBuffer

abstract class Vehicle(company: Company, engine : Engine, private var _pos : Pos) extends Item(company) with Updatable {

  super.updateRate_=(UpdateRate.TRAIN_UPDATE)

  def pos : Pos = _pos
  def pos_= (value : Pos) : Unit = _pos = value
  def pos_= (x : Double, y : Double) : Unit = _pos = new Pos(x, y)

  var dir : Dir = new Dir(0, 0)

  var carriages : ListBuffer[Carriage] = ListBuffer.empty

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

  def setObjective[T <: TransportFacility]
  (transportFacility : T, from : Pos) : Unit = {
    if (goalTransportFacility.nonEmpty) return

    goalTransportFacility = Some(transportFacility)

    dir.x = transportFacility.pos.x - from.x
    dir.y = transportFacility.pos.y - from.y
    dir.normalize()
  }

  def unsetObjective() : Unit = {
    goalTransportFacility = None
  }

  def setDestination(town : Town) : Unit = {
    destination = Some(town)
  }

  def putOnRoad[R <: Road](road : R) : Boolean = {
    //TODO Change to EXCP
    if (road.isFull) return false
    currentRoad match {
      case Some(_) => return false
      case None =>
        road.addVehicle(this)
        currentRoad = Some(road)
    }
    true
  }

  def removeFromRoad() : Unit = {
    currentRoad match {
      case Some(road) =>
        road.removeVehicle(this)
        currentRoad = None
      case None =>
    }
  }

}
