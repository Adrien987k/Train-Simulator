package interface

import game.Game
import logic.Updatable
import logic.items.transport.vehicules.Vehicle
import utils.Pos

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

object LocalInformationPanel extends GUIComponent {

  private val localInformationPane : BorderPane = new BorderPane
  private val noInfoLabel = new Label("No town selected")

  private var currentUpdatable : Option[Updatable] = None

  def make() : Node = {
    localInformationPane.style = "-fx-background-color: lightCoral"
    localInformationPane.center = noInfoLabel
    localInformationPane
  }

  override def restart(): Unit = {
    localInformationPane.center = noInfoLabel
    currentUpdatable = None
  }

  override def update() : Unit = {
    currentUpdatable match {
      case Some(updatable) =>
        localInformationPane.center = updatable.propertyPane()

      case None => localInformationPane.center = noInfoLabel
    }
  }

  def selectUpdatableAt(pos : Pos): Unit = {
    Game.world.updatableAt(pos) match {
      case Some(vehicle : Vehicle) =>
        OneVehicleInformationPanel.addPanel(vehicle.propertyPane())
        WorldCanvas.selectTrain(vehicle)

      case Some(updatable) => currentUpdatable = Some(updatable)

      case None =>
    }
  }

}
