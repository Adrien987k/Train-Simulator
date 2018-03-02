package interface

import engine.{CannotBuildItemException, ItemType, World}
import interface.WorldCanvas.gc
import link.{Change, CreationChange, Observer}
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.event.EventIncludes
import scalafx.scene.Node
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.control.ScrollPane
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color

sealed abstract class Item(pos1 : Pos) { }

case class TOWN(pos1 : Pos, level : Int) extends Item(pos1 : Pos)
case class RAIL(pos1 : Pos, pos2 : Pos) extends Item(pos1 : Pos)
case class TRAIN(pos1 : Pos) extends Item(pos1 : Pos)

object WorldCanvas extends Observer with GUIComponent {

  val TOWN_RADIUS = 10
  val TRAIN_RADIUS = 5

  var canvas = new Canvas(World.MAP_WIDTH, World.MAP_HEIGHT)
  var gc: GraphicsContext = canvas.graphicsContext2D
  var items: ListBuffer[Item] = ListBuffer.empty
  var selectedItem: Option[Item] = None

  var lastPosClicked = new Pos(0,0)

  def make(): Node = {
    val scrollPane = new ScrollPane
    scrollPane.content = canvas
    scrollPane
  }

  def restart(): Unit = {
    items = ListBuffer.empty
    selectedItem = None
  }

  def initWorld(townsPositions : List[Pos]): Unit = {
    canvas.onMouseClicked = (event: MouseEvent) => {
      lastPosClicked = new Pos(event.x.toInt, event.y.toInt)
      if (ItemsButtonBar.selected != null)
        ItemsButtonBar.selected match {
          case Some(item) => World.company.tryPlace(item, lastPosClicked)
          case None => LocalInformationPanel.displayElementInfoAt(lastPosClicked)
        }
    }

    canvas.onMouseMoved = (event: MouseEvent) => {
      val pos = new Pos(event.x.toInt, event.y.toInt)
      var newItemSelected = false
      items.foreach {
        case TOWN(pos1, level) =>
          if (pos.inRange(pos1, TOWN_RADIUS * 3)) {
            selectedItem = Some(TOWN(pos1, level))
            newItemSelected = true
          }
        case RAIL(pos1, pos2) =>
          if (pos.inLineRange(pos1, pos2, 10) && selectedItem.isEmpty) {
            selectedItem = Some(RAIL(pos1, pos2))
            newItemSelected = true
          }
        case TRAIN(pos1) =>
          //TODO When train selected
      }
      if (!newItemSelected) selectedItem = None
    }

    this.register(World)
    this.register(World.company)

    gc.fill = Color.Red
    townsPositions.foreach(pos => {
      items += TOWN(pos, 0)
      gc.fillOval(pos.x, pos.y, TOWN_RADIUS * 2, TOWN_RADIUS * 2)
    })
  }

  def update(): Unit = {
    gc.fill = Color.White
    gc.fillRect(0, 0, canvas.width(), canvas.height())
    gc.stroke = Color.Black
    gc.lineWidth = 3
    items.foreach {
      case town: TOWN =>
        gc.fill = if (town.level == 0) Color.Red else Color.Green
        gc.fillOval(town.pos1.x - TOWN_RADIUS, town.pos1.y - TOWN_RADIUS, TOWN_RADIUS * 2, TOWN_RADIUS * 2)
      case rail: RAIL =>
        if (selectedItem.nonEmpty) {
          selectedItem.get match {
            case srail: RAIL =>
              if (!srail.pos1.equals(rail.pos1) || !srail.pos2.equals(rail.pos2))
                gc.strokeLine(rail.pos1.x, rail.pos1.y, rail.pos2.x, rail.pos2.y)
            case _ =>
              gc.strokeLine(rail.pos1.x, rail.pos1.y, rail.pos2.x, rail.pos2.y)
          }
        } else {
          gc.strokeLine(rail.pos1.x, rail.pos1.y, rail.pos2.x, rail.pos2.y)
        }
      case train: TRAIN =>
        gc.fill = Color.Blue
        gc.fillRect(train.pos1.x - TRAIN_RADIUS, train.pos1.y - TRAIN_RADIUS, TRAIN_RADIUS * 2, TRAIN_RADIUS * 2)
    }
    if (selectedItem.nonEmpty) {
      selectedItem.get match {
        case town : TOWN =>
          gc.strokeRect(town.pos1.x - TOWN_RADIUS * 1.25, town.pos1.y - TOWN_RADIUS * 1.25, TOWN_RADIUS * 2.5, TOWN_RADIUS * 2.5)
        case rail : RAIL =>
          gc.lineWidth = 10
          gc.stroke = Color.Gold
          gc.strokeLine(rail.pos1.x, rail.pos1.y, rail.pos2.x, rail.pos2.y)
        case _ =>
      }
    }
    items.filter(item => item match {
      case _: TRAIN => false
      case _ => true
    })
  }

  override def notifyChange(changes: ListBuffer[Change]): Unit = {
    changes.foreach {
      case cch: CreationChange =>
        cch.itemType match {
          case ItemType.STATION =>
            println("NOTIFIED STATION")
            items = items.map {
              case town: TOWN =>
                if (town.pos1.equals(cch.pos1)) {
                  TOWN(town.pos1, 1)
                }
                else town
              case o => o
            }
          case ItemType.RAIL =>
            items += RAIL(cch.pos1, cch.pos2)
          case ItemType.TRAIN =>
            items += TRAIN(cch.pos1)
        }
    }
  }
}
