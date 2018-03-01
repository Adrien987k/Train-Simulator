package engine

import interface.GUI
import utils.Pos

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicTrain(_pos : Pos) extends Train(_pos : Pos) {

  override def place(): Unit = {

  }

  override def render(): Unit = {
    GUI.displayTrain(pos)
  }

  override def step(): Unit = {
    goalStation match {
      case Some(station) =>
        if (pos.inRange(station.pos, 10)) {
          station.unload(this)
        } else {
          pos.x = pos.x + (dir.x * speed)
          pos.y = pos.y + (dir.y * speed)
        }
      case None =>
    }

    //
    render()
  }

  override def setObjective(station: Station, from: Pos, nbPassengers : Int): Unit = {
      if (goalStation.nonEmpty) return

      goalStation = Some(station)
      dir.x = station.pos.x - from.x
      dir.x = station.pos.y - from.y

      this.nbPassenger = nbPassengers
  }


  override def putOnRail(rail: Rail): Boolean = {
    if (rail.isFull) return false
    currentRail match {
      case Some(_) => return false
      case None =>
    rail.addTrain(this)
    currentRail = Some(rail)
    }
    true
  }

  override def removeFromRail(): Unit = {
    currentRail match {
      case Some(rail) =>
    rail.removeTrain(this)
    currentRail = None
      case None =>
    }
  }

  override def propertyPane(): Node = {
    val panel = new VBox()
    val speedLabel = new Label("Maw capacity : " + speed)
    val sizeLabel = new Label("Trains : " + size)
    val maxWeightLabel = new Label("Length : " + maxWeight)
    val nbPassengerLabel = new Label("Passengers : " + nbPassenger)
    val posLabel = new Label("position : " + pos)
    panel.children = List(speedLabel, sizeLabel, maxWeightLabel, nbPassengerLabel)
    val goalStationLabel = new Label("Goal station : ")
    if (goalStation.nonEmpty)
      goalStationLabel.text = "Goal station : " + goalStation.get.town.name
    panel.children.add(goalStationLabel)
    panel
  }

  override def toString: String = {
    "Speed : " + speed + "\n" +
      "Size : " + size + "\n" +
      "Max Weight : " + maxWeight + "\n" +
      "nbPassenger : " + nbPassenger + "\n" +
      (if (goalStation.isEmpty)  "No Goal Station"
      else "Goal Station : " + goalStation.get.town.name)
  }
}
