package logic.world

import game.Game
import logic.exceptions.CannotBuildItemException
import logic.items.ItemTypes
import logic.items.transport.facilities.{Station, TransportFacility}
import logic.items.transport.roads.{BasicRail, Road}
import logic.items.transport.vehicules.{Train, Vehicle}
import logic.world.towns.Town
import interface.{GlobalInformationPanel, ItemsButtonBar}
import link.{CreationChange, Observable}
import logic.items.ItemTypes.ItemType
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.collections.ObservableBuffer

abstract class Company(world : World) extends Observable {

  var money = 20000.0
  var ticketPricePerKm = 0.01

  val vehicles : ObservableBuffer[Vehicle] = ObservableBuffer.empty
  val roads :  ListBuffer[Road] = ListBuffer.empty

  private var lastStation : Option[Station] = None
  private var selectedTrain: Option[Train] = None

  def tryPlace(itemType: ItemType, pos : Pos): Unit = {
    GlobalInformationPanel.removeWarningMessage()
    val elem = world.updatableAt(pos) match {
      case Some(e) => e
      case None => return
    }
    try {
      var quantity = 0
      if (!canBuy(itemType)) throw new CannotBuildItemException("Not enough money")
      (itemType, elem) match {
        case (ItemTypes.STATION, town : Town) =>
          town.buildStation()
          addChange(new CreationChange(town.pos, null, ItemTypes.STATION))
        case (ItemTypes.DIESEL_TRAIN, town : Town) =>
          town.buildTrain()
        case (ItemTypes.RAIL, town : Town) =>
          if (!town.hasStation) throw new CannotBuildItemException("This town does not have a station")
          lastStation match {
            case Some(station) =>
              if (station == town.station.get) return
              if (roadAlreadyExist(station, town.station.get))
                throw new CannotBuildItemException("This rail already exists")
              lastStation = None
              quantity = station.pos.dist(town.station.get.pos).toInt
              if (!canBuy(itemType, quantity)) throw new CannotBuildItemException("Not enough money")
              buildRail(station, town.station.get)
              addChange(new CreationChange(station.pos, town.station.get.pos, ItemTypes.RAIL))
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
      world.updatableAt(pos) match {
        case Some(town : Town) =>
          train.setDestination(town)
        case _ =>
      }
    )
  }

  /**
    * @return True if a road already exist between [transportFacilityA] and [transportFacilityB]
    */
  private def roadAlreadyExist(transportFacilityA : TransportFacility, transportFacilityB : TransportFacility) : Boolean = {
    roads.exists(road =>
      road.transportFacilityA == transportFacilityA && road.transportFacilityB == transportFacilityB
      ||
      road.transportFacilityB == transportFacilityA && road.transportFacilityA == transportFacilityB)
  }

  private def canBuy(itemType : ItemType, quantity : Int = 1) : Boolean = {
    val price = Shop.price(itemType, quantity)
    money - price >= 0
  }

  def buy(itemType: ItemType, quantity : Int = 1): Unit = {
    val price = Shop.price(itemType, quantity)
    if (money - price < 0)
      throw new CannotBuildItemException("Not enough money")
    else {
      money -= price
    }
  }

  def buildRail(stationA : Station, stationB : Station): Unit = {
    val rail = new BasicRail(this, stationA, stationB)
    roads += rail
    stationA.addRail(rail)
    stationB.addRail(rail)
  }

}
