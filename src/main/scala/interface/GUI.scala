package interface

import engine.{ItemType, Town, World}

import scalafx.Includes._
import scala.collection.mutable.ListBuffer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Orientation
import scalafx.scene.Scene
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
      rootPane.center = makeMainSplitPain()
      rootPane.bottom = makeItemsButtonsBar()
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
    menuBar
  }

  private def makeMainSplitPain(): SplitPane = {
    val leftSplit = new SplitPane
    leftSplit.orientation = Orientation.Vertical

    val globalInfoPane = new BorderPane
    globalInfoPane.center = new GlobalInformationTextArea
    val localInfoPane = new BorderPane
    localInfoPane.center = new LocalInformationTextArea
    leftSplit.items ++= List(globalInfoPane, localInfoPane)

    val rightBorder = new BorderPane

    rightBorder.center = WorldCanvas.makeWorldCanvas()

    val topSplit = new SplitPane
    topSplit.orientation = Orientation.Horizontal
    topSplit.items ++= List(leftSplit, rightBorder)
    topSplit.dividerPositions = 0.3

    topSplit
  }

  var selected:Option[ItemType.Value] = None

  def select(itemType: ItemType.Value = null): Unit = {
    selected = Option(itemType)
  }

  private def makeItemsButtonsBar(): ButtonBar = {
    val bar : ButtonBar = new ButtonBar
    var itemButtons : ListBuffer[Button] = ListBuffer.empty

    for (item <- ItemType.values) {
      val itemButton = new Button(item.toString)
      itemButton.onAction = _ => {
        select(item)
      }
      itemButtons += itemButton
    }

    bar.buttons = itemButtons
    bar
  }

  private var lastTime = 0L

  def initWorldCanvas(towns: ListBuffer[Town]): Unit = {
    WorldCanvas.initWorld(towns.map(town => town.pos).toList)

    val timer = AnimationTimer { time =>
      if (time - lastTime / 1e9 >= 1)
        World.update()

        WorldCanvas.update()
      lastTime = time
    }
    timer.start()
  }

  //Ad
  def displayTrain(pos : utils.Pos): Unit = {

  }

}
