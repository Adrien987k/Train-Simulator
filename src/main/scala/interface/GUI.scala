package interface

import engine.{Town, World}

import scala.collection.mutable.ListBuffer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Orientation
import scalafx.scene.{Node, Scene}
import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.event.ActionEvent
import scalafx.scene.control._

object GUI extends JFXApp {

  stage = new PrimaryStage {
    title = "Train simulator"
    scene = new Scene(1200, 700) {
      val rootPane = new BorderPane
      rootPane.top = makeMenu()
      rootPane.center = makeCentralPain()
      rootPane.bottom = ItemsButtonBar.make()
      root = rootPane
    }
  }

  World.init()

  private def makeMenu(): MenuBar = {
    val menuBar = new MenuBar
    val mapMenu = new Menu("Map")
    val newGameItem = new MenuItem("New Game")
    val saveItem = new MenuItem("Save Map")
    val loadItem = new MenuItem("Save Map")
    val exitItem = new MenuItem("Exit")
    mapMenu.items = List(newGameItem, saveItem, loadItem, new SeparatorMenuItem(), exitItem)
    menuBar.menus = List(mapMenu)

    newGameItem.onAction = (_ : ActionEvent) => {
      World.newGame()
    }

    saveItem.onAction = (_ : ActionEvent) => {
      //TODO save the current map in a file
    }

    loadItem.onAction = (_ : ActionEvent) => {
      //TODO load a map from a file
    }

    exitItem.onAction = (_ : ActionEvent) => {
      sys.exit(0)
    }
    menuBar
  }

  private def makeCentralPain(): Node = {
    val leftSplit = new SplitPane
    leftSplit.orientation = Orientation.Vertical
    val globalInfoPane = new BorderPane
    globalInfoPane.center = GlobalInformationPanel.make()
    val localInfoPane = new BorderPane
    localInfoPane.center = LocalInformationPanel.make()
    leftSplit.items ++= List(globalInfoPane, localInfoPane)
    val rightSplit = new SplitPane


    rightSplit.orientation = Orientation.Vertical
    val trainsPane = AllTrainsInformationPanel.make()
    val oneTrainPane = OneTrainInformationPanel.make()

    rightSplit.items ++= List(trainsPane, oneTrainPane)

    val centralSplit = new SplitPane
    centralSplit.orientation = Orientation.Horizontal
    centralSplit.setDividerPositions(0.15, 0.85)
    centralSplit.items ++= List(leftSplit, WorldCanvas.make(), rightSplit)

    centralSplit.requestFocus()
    centralSplit
  }

  def restart(): Unit = {
    WorldCanvas.restart()
    LocalInformationPanel.restart()
  }

  private var lastTime = 0L

  def initWorldCanvas(towns: ListBuffer[Town]): Unit = {
    WorldCanvas.initWorld(towns.map(town => town.pos).toList)

    val timer = AnimationTimer { time =>
      World.update()
      GlobalInformationPanel.update(time)
      WorldCanvas.update()
      lastTime = time
    }
    timer.start()
  }

}
