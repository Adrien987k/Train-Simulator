package logic.items.transport.facilities

import logic.Updatable
import logic.items.transport.roads.Rail
import logic.items.transport.vehicules.BasicTrain
import logic.world.Company
import logic.world.towns.Town
import utils.Pos

abstract class Station
(company: Company,
 _pos : Pos,
 _town : Town)
  extends TransportFacility(company, _pos, _town) with Updatable {

  def addRail(rail : Rail): Unit = {
    super.addRoad(rail)
  }

  def buildTrain() : Boolean = {
    val train = new BasicTrain(company, pos.copy())
    addVehicle(train)
  }

  def availableTrains() : Int = super.availableVehicles

}
