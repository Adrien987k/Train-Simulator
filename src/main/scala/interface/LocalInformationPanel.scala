package interface

import engine.World
import utils.Pos

import scalafx.scene.Node
import scalafx.scene.control.{Label, TextArea}
import scalafx.scene.layout.BorderPane

object LocalInformationPanel {

  private var localInformationPane: BorderPane = new BorderPane
  private val noInfoLabel = new Label("No element selected")

  def makeLocalnformationPanel() : Node = {
    localInformationPane
  }

  def restart(): Unit = {
    localInformationPane.center = noInfoLabel
  }

  def displayElementInfoAt(pos : Pos): Unit = {
    World.updatableAt(pos) match {
      case Some(e) => localInformationPane.center = e.propertyPane()
      case None => localInformationPane.center = noInfoLabel
    }
  }

}
