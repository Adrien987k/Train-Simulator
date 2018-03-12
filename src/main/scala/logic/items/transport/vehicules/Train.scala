package logic.items.transport.vehicules

import logic.items.transport.facilities.Station
import logic.items.transport.roads.Rail
import logic.world.Company
import utils.Pos

abstract class Train(val company: Company, val engine : Engine, _pos : Pos) extends Vehicle(company, engine, _pos) {

  def setObjective(station : Station, from : Pos) : Unit = {
    super.setObjective(station, from)
  }

  def putOnRail(rail : Rail) : Boolean = {
    super.putOnRoad(rail)
  }

  def removeFromRail() : Unit = {
    super.removeFromRoad()
  }
}
