package interface
import interface.LocalInformationPanel.noInfoLabel
import logic.items.transport.vehicules.Vehicle

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import scalafx.scene.text.{Font, FontWeight}

object OneVehicleInformationPanel extends GUIComponent {


  val noInfoLabel : Label = new Label("No vehicle selected")
  var panel : BorderPane = new BorderPane

  var currentVehicle : Option[Vehicle] = None

  override def make() : Node = {
    noInfoLabel.font = Font.font(null, FontWeight.Bold, 18)

    panel.center = noInfoLabel
    panel.style = "-fx-background-color: lightGreen"
    panel
  }

  override def restart() : Unit = {
    panel.center = noInfoLabel
    removeCurrentVehicle()
  }

  override def update() : Unit = {
    currentVehicle match {
      case Some(vehicle) =>
        panel.center = vehicle.propertyPane()

      case None => panel.center = noInfoLabel
    }
  }

  def addCurrentVehicle(vehicle : Vehicle) : Unit = {
    currentVehicle = Some(vehicle)
  }

  def removeCurrentVehicle() : Unit = {
    currentVehicle = None
  }

}
