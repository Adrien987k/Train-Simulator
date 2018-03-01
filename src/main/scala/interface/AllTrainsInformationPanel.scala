package interface

import engine.World

import scalafx.Includes._
import scala.collection.mutable.ListBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Orientation
import scalafx.scene.Node
import scalafx.scene.control.{Button, ButtonBar, ScrollPane}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.VBox

object AllTrainsInformationPanel extends GUIComponent {

  val panel = new ScrollPane()
  val trainButtonsBox = new VBox()

  def make(): Node = {
    panel.content = trainButtonsBox
    trainButtonsBox.setFillWidth(true)
    panel.requestFocus()
    panel
  }

  def update(): Unit = {
    var trainButtons : ListBuffer[Button] = ListBuffer.empty
    World.company.trains.foreach(train =>
      {
        val trainButton = new Button("Train")
        trainButton.maxWidth = Double.MaxValue
        trainButton.onAction = (event : ActionEvent) => {
          //TODO Display info about the train
          println("TRAIN")
        }
        trainButtons += trainButton
      }
    )
    trainButtonsBox.children = trainButtons
  }

}
