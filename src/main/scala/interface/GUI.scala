package interface

import engine.Town
import link.Change
import utils.Pos

import scalafx.Includes._
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Orientation, Pos}
import scalafx.scene.{Node, Scene}
import scalafx.scene.effect.{DropShadow, InnerShadow}
import scalafx.scene.layout._
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Ellipse
import scalafx.scene.paint.Color
import javafx.beans.{binding => jfxbb}

import scala.collection.JavaConverters._
import scalafx.Includes._
import scalafx.Includes.when
import scalafx.beans.binding._
import scalafx.beans.property.ObjectProperty
import scalafx.event.ActionEvent
import scalafx.scene.canvas.Canvas
import scalafx.scene.control._

object GUI extends JFXApp {

  stage = new PrimaryStage {
    title = "Train simulator"
    scene = new Scene(1000, 700) {
      val menuBar = new MenuBar
      val mapMenu = new Menu("Map")
      val newGameItem = new MenuItem("New Game")
      val saveItem = new MenuItem("Save Map")
      val loadItem = new MenuItem("Save Map")
      val exitItem = new MenuItem("Exit")
      mapMenu.items = List(newGameItem, saveItem, loadItem, new SeparatorMenuItem(), exitItem)
      menuBar.menus = List(mapMenu)

      newGameItem.onAction = (event : ActionEvent) => {
        //TODO Create a new game
      }

      saveItem.onAction = (event : ActionEvent) => {
        //TODO save the current map in a file
      }

      loadItem.onAction = (event : ActionEvent) => {
        //TODO load a map from a file
      }

      exitItem.onAction = (event : ActionEvent) => {
        //TODO save the map
        sys.exit(0)
      }

      val splitPane: SplitPane = makeMainSplitPain()

      val rootPane = new BorderPane
      rootPane.top = menuBar
      rootPane.center = splitPane
      rootPane.bottom = new ItemsButtonBar
      root = rootPane
    }
  }

  private def makeMainSplitPain(): SplitPane = {
    val leftSplit = new SplitPane
    leftSplit.orientation = Orientation.Vertical

    leftSplit.items ++= List(new GlobalInformationTextArea, new LocalInformationTextArea)

    val rightBorder = new BorderPane

    rightBorder.center = new WorldCanvas

    val topSplit = new SplitPane
    topSplit.orientation = Orientation.Horizontal
    topSplit.items ++= List(leftSplit, rightBorder)
    topSplit.dividerPositions = 0.3

    topSplit
  }

  //Eng
  def initWorld(towns: ListBuffer[Town]): Unit = {
    //
  }

  //Eng
  def display(changes: ListBuffer[Change]): Unit = {
    for (change <- changes) {
      //Apply change
    }
    //Effacer les trains
  }

  //Ad
  def displayTrain(pos : utils.Pos): Unit = {

  }

}
