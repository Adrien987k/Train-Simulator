package interface

import game.Game
import logic.items.transport.vehicules.Vehicle

import scala.collection.mutable
import scalafx.Includes._
import scalafx.collections.ObservableBuffer.{Add, Remove}
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.layout.VBox

object AllVehiclesInformationPanel extends GUIComponent {

  val panel = new ScrollPane()
  val vehicleButtonsBox = new VBox()

  var vehicleButtonsMap : mutable.Map[Vehicle, Button] = mutable.Map.empty

  var nbVehicle = 0

  def make(): Node = {
    vehicleButtonsBox.setFillWidth(true)
    panel.fitToWidth = true
    panel.content = vehicleButtonsBox
    vehicleButtonsBox.style = "-fx-background-color: moccasin"
    panel.style = "-fx-background-color: moccasin"
    panel
  }

  def restart(): Unit = {
    vehicleButtonsMap = mutable.Map.empty
  }

  Game.world.company.vehicles.onChange(
    (_, changes) =>
      changes.foreach {
        case Add(_, added) =>
          added.foreach(addVehicleButton)
        case Remove(_, removed) =>
          removed.foreach(removeTrainButton)
        case _ =>
      }
  )

  def keyForValue(value: Button): Option[Vehicle] = {
    vehicleButtonsMap.find({case (_, button) => button  == value}) match {
      case Some((vehicle, _)) => Some(vehicle)
      case None => None
    }
  }

  def addVehicleButton(vehicle : Vehicle): Unit = {
    nbVehicle += 1

    val vehicleButton = new Button(vehicle.vehicleType.name + nbVehicle)
    vehicleButton.maxWidth = Double.MaxValue

    vehicleButton.onAction = (_ : ActionEvent) => {
      keyForValue(vehicleButton) match {
        case Some(vehicleFromButton) =>
          OneVehicleInformationPanel.addPanel(vehicleFromButton.propertyPane())
          WorldCanvas.selectTrain(vehicleFromButton)
        case None =>
      }
    }

    vehicleButtonsMap.+=((vehicle, vehicleButton))
    vehicleButtonsBox.children.add(vehicleButton)
  }

  def removeTrainButton(vehicle : Vehicle): Unit = {
    vehicleButtonsBox.children.remove(vehicleButtonsMap(vehicle))
    vehicleButtonsMap.-=(vehicle)
  }

}
