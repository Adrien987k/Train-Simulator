package engine

import interface.WorldCanvas
import utils.Pos

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox

class BasicTrain(company: Company, _pos : Pos) extends Train(company: Company, _pos : Pos) {

  private var counter = 0

  override def step(): Unit = {
    counter += 1
    if (counter == UpdateRate.TRAIN_UPDATE) {
      goalStation match {
        case Some(station) =>
          if (pos.inRange(station.pos, 10)) {
            removeFromRail()
            station.unload(this)
          } else {
            pos.x += dir.x * speed
            pos.y += dir.y * speed
          }
        case None =>
      }
      counter = 0
    }
  }

  override def setObjective(station: Station, from: Pos): Unit = {
      if (goalStation.nonEmpty) return

      goalStation = Some(station)

      dir.x = station.pos.x - from.x
      dir.y = station.pos.y - from.y
      dir.normalize()
  }

  override def setDestination(town : Town): Unit = {
    println("DESTINATION : " + town.name)
    destination = Some(town)
  }

  override def unsetObjective(): Unit = {
    goalStation = None
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
    val speedLabel = new Label("Speed : " + speed)
    val sizeLabel = new Label("Size : " + size)
    val maxWeightLabel = new Label("Max weight : " + maxWeight)
    val maxPassengerLabel = new Label("Max passengers : " + passengerCapacity)
    val nbPassengerLabel = new Label("Passengers : " + nbPassenger)
    val posLabel = new Label("Position : " + pos)
    panel.children = List(speedLabel, sizeLabel, maxWeightLabel, maxPassengerLabel, nbPassengerLabel, posLabel)
    val goalStationLabel = new Label("Goal station : ")
    if (goalStation.nonEmpty)
      goalStationLabel.text = "Goal station : " + goalStation.get.town.name
    panel.children.add(goalStationLabel)
    val chooseDestPanel = new Button("Choose destination")
    chooseDestPanel.onAction = (_ : ActionEvent) => {
      World.company.selectTrain(this)
      WorldCanvas.activeDestinationChoice()
    }
    panel.children.add(chooseDestPanel)
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
