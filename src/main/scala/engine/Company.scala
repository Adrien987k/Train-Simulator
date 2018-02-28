package engine

import link.{CreationChange, Observable}
import utils.Pos

import scala.collection.mutable.ListBuffer

class Company extends Observable {

  var money = 100000

  val trains : ListBuffer[Train] = ListBuffer.empty
  val rails :  ListBuffer[Rail] = ListBuffer.empty

  private var lastStation : Option[Station] = None

  def tryPlace(itemType: ItemType.Value, pos : Pos): Unit = {
    println("TRY PLACE : " + itemType.toString + " (" + pos.x + ", " + pos.y + ")")
    val elem = World.updatableAt(pos) match {
      case Some(e) =>
        println("SOME")
        e
      case None => return
    }
    try {
      if (!canBuy(itemType)) throw new CannotBuildItemException("Not enough money")
      (itemType, elem) match {
        case (ItemType.STATION, town : Town) =>
          town.buildStation()
          addChange(new CreationChange(town.pos, null, ItemType.STATION))
        case (ItemType.TRAIN, town : Town) =>
          town.buildTrain()
        case (ItemType.RAIL, town : Town) =>
          if (!town.hasStation) throw new CannotBuildItemException("This town does not have a station")
          lastStation match {
            case Some(station) =>
              lastStation = None
              buildRail(station, town.station.get)
              addChange(new CreationChange(station.pos, town.station.get.pos, ItemType.RAIL))
            case None =>
              lastStation = Some(town.station.get)
              return
          }
        case _ => return
      }
      buy(itemType)
    } catch {
      case e : CannotBuildItemException =>
        //TODO display e.getMessage
    }
    notifyObservers()
  }

  private def railAlreadyExist(stationA : Station, stationB : Station) : Boolean = {
    rails.exists(rail => rail.stationA == stationA && rail.stationB == stationB
      | rail.stationB == stationA && rail.stationA == stationB)
  }

  private def canBuy(itemType: ItemType.Value) : Boolean = {
    val price = Shop.price(itemType)
    money - price >= 0
  }

  def buy(itemType: ItemType.Value): Unit = {
    val price = Shop.price(itemType)
    if (money - price < 0)
      throw new CannotBuildItemException("Not enough money")
    else {
      money -= price
    }
  }

  def buildRail(stationA : Station, stationB : Station): Unit = {
    val rail = new BasicRail(stationA, stationB)
    rails += rail
    //TODO Generate Change for the GUI
  }

}
