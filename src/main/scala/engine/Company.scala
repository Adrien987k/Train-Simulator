package engine

import interface.{GlobalInformationPanel, ItemsButtonBar}
import link.{CreationChange, Observable}
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.collections.ObservableBuffer

class Company extends Observable {

  var money = 20000.0
  var ticketPricePerKm = 0.01

  val trains : ObservableBuffer[Train] = ObservableBuffer.empty
  val rails :  ListBuffer[Rail] = ListBuffer.empty

  private var lastStation : Option[Station] = None
  private var selectedTrain: Option[Train] = None

  def tryPlace(itemType: ItemType.Value, pos : Pos): Unit = {
    GlobalInformationPanel.removeWarningMessage()
    val elem = World.updatableAt(pos) match {
      case Some(e) => e
      case None => return
    }
    try {
      var quantity = 0
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
              if (station == town.station.get) return
              if (railAlreadyExist(station, town.station.get))
                throw new CannotBuildItemException("This rail already exists")
              lastStation = None
              quantity = station.pos.dist(town.station.get.pos).toInt
              if (!canBuy(itemType, quantity)) throw new CannotBuildItemException("Not enough money")
              buildRail(station, town.station.get)
              addChange(new CreationChange(station.pos, town.station.get.pos, ItemType.RAIL))
            case None =>
              lastStation = Some(town.station.get)
              return
          }
        case _ => return
      }
      buy(itemType, quantity)
      ItemsButtonBar.select()
    } catch {
      case e : CannotBuildItemException =>
        GlobalInformationPanel.displayWarningMessage(e.getMessage)
    }
    notifyObservers()
  }

  def selectTrain(train : Train): Unit = {
    selectedTrain = Some(train)
  }

  def setTrainDestination(pos : Pos): Unit = {
    selectedTrain.foreach(train =>
      World.updatableAt(pos) match {
        case Some(town : Town) =>
          train.setDestination(town)
        case _ =>
      }
    )
  }

  private def railAlreadyExist(stationA : Station, stationB : Station) : Boolean = {
    rails.exists(rail => rail.stationA == stationA && rail.stationB == stationB
      | rail.stationB == stationA && rail.stationA == stationB)
  }

  private def canBuy(itemType: ItemType.Value, quantity : Int = 1) : Boolean = {
    val price = Shop.price(itemType, quantity)
    money - price >= 0
  }

  def buy(itemType: ItemType.Value, quantity : Int = 1): Unit = {
    val price = Shop.price(itemType, quantity)
    if (money - price < 0)
      throw new CannotBuildItemException("Not enough money")
    else {
      money -= price
    }
  }

  def buildRail(stationA : Station, stationB : Station): Unit = {
    val rail = new BasicRail(this, stationA, stationB)
    rails += rail
    stationA.addRail(rail)
    stationB.addRail(rail)
  }

}
