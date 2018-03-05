package interface

import engine.{Train, World}

import scala.collection.mutable
import scalafx.Includes._
import scalafx.collections.ObservableBuffer.{Add, Remove}
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.layout.VBox

object AllTrainsInformationPanel extends GUIComponent {

  val panel = new ScrollPane()
  val trainButtonsBox = new VBox()

  val trainButtonsMap : mutable.Map[Train, Button] = mutable.Map.empty

  def make(): Node = {
    trainButtonsBox.setFillWidth(true)
    panel.fitToWidth = true
    panel.content = trainButtonsBox
    panel
  }

  World.company.trains.onChange(
    (_, changes) =>
      changes.foreach {
        case Add(_, added) =>
          added.foreach(addTrainButton)
        case Remove(_, removed) =>
          removed.foreach(removeTrainButton)
        case _ =>
      }
  )

  def keyForValue(value: Button): Option[Train] = {
    trainButtonsMap.find({case (_, button) => button  == value}) match {
      case Some((train, _)) => Some(train)
      case None => None
    }
  }

  def addTrainButton(train : Train): Unit = {
    val trainButton = new Button("Train")
    trainButton.maxWidth = Double.MaxValue
    trainButton.onAction = (_ : ActionEvent) => {
      keyForValue(trainButton) match {
        case Some(t) =>
          OneTrainInformationPanel.addPanel(t.propertyPane())
          WorldCanvas.selectTrain(t.pos)
        case None =>
      }
    }
    trainButtonsMap.+=((train, trainButton))
    trainButtonsBox.children.add(trainButton)
  }

  def removeTrainButton(train : Train): Unit = {
    trainButtonsBox.children.remove(trainButtonsMap(train))
    trainButtonsMap.-=(train)
  }

}
