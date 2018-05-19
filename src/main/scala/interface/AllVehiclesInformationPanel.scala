package interface

import logic.items.transport.vehicules.Vehicle

import scala.collection.mutable
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Font, FontWeight}

object AllVehiclesInformationPanel extends GUIComponent {

  val panel = new ScrollPane()
  var vehicleButtonsBox = new VBox()

  var vehicleButtonsMap : mutable.Map[Vehicle, Button] = mutable.Map.empty

  var nbVehicle = 0

  def make() : Node = {
    vehicleButtonsBox.setFillWidth(true)

    panel.fitToWidth = true
    panel.content = vehicleButtonsBox

    vehicleButtonsBox.style = "-fx-background-color: moccasin"

    panel.style = "-fx-background-color: moccasin"
    panel
  }

  override def restart() : Unit = {
    vehicleButtonsMap = mutable.Map.empty
    vehicleButtonsBox.children.remove(0, vehicleButtonsBox.children.size())

    nbVehicle = 0
  }

  override def update() : Unit = {

  }

  def addVehiclePanel(vehicle : Vehicle) : Unit = {
    addVehicleButton(vehicle)
  }

  def removeVehiclePanel(vehicle : Vehicle) : Unit = {
    removeVehicleButton(vehicle)
  }

  def keyForValue(value: Button): Option[Vehicle] = {
    vehicleButtonsMap.find({case (_, button) => button  == value}) match {
      case Some((vehicle, _)) => Some(vehicle)
      case None => None
    }
  }

  def addVehicleButton(vehicle : Vehicle): Unit = {
    nbVehicle += 1

    val vehicleButton = new Button(vehicle.vehicleType.name + " " + nbVehicle)
    vehicleButton.maxWidth = Double.MaxValue
    vehicleButton.font = Font.font(null, FontWeight.Bold, 18)

    vehicleButton.onAction = (_ : ActionEvent) => {
      keyForValue(vehicleButton) match {
        case Some(vehicleFromButton) =>
          OneVehicleInformationPanel.addCurrentVehicle(vehicleFromButton)
          WorldCanvas.selectTrain(vehicleFromButton)
        case None =>
      }
    }

    vehicleButtonsMap.+=((vehicle, vehicleButton))
    vehicleButtonsBox.children.add(vehicleButton)
  }

  def removeVehicleButton(vehicle : Vehicle): Unit = {
    vehicleButtonsBox.children.remove(vehicleButtonsMap(vehicle))
    vehicleButtonsMap.-=(vehicle)
  }

}
