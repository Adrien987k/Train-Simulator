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

object WorldCanvas extends Observer {

  val TOWN_RADIUS = 10

  var canvas = new Canvas(World.MAP_WIDTH, World.MAP_HEIGHT)
  var gc: GraphicsContext = canvas.graphicsContext2D
  var items: ListBuffer[Item] = ListBuffer.empty
  var selectedItem: Option[Item] = None

  var lastPosCliked = new Pos(0,0)

  def makeWorldCanvas(): Node = {
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
      lastPosCliked = new Pos(event.x.toInt, event.y.toInt)
      if (ItemsButtonBar.selected != null)
        ItemsButtonBar.selected match {
          case Some(item) => World.company.tryPlace(item, lastPosCliked)
          case None => LocalInformationPanel.displayElementInfoAt(lastPosCliked)
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
          if ((pos2.x - pos1.x) != 0 && (pos.x - pos1.x) != 0 &&
            math.abs(((pos2.y - pos1.y).toDouble / (pos2.x - pos1.x).toDouble) - ((pos.y - pos1.y).toDouble / (pos.x - pos1.x).toDouble)) <= 0.1 &&
            ((pos.x <= pos2.x && pos.x >= pos1.x) || (pos.x >= pos2.x && pos.x <= pos1.x)) &&
              selectedItem.isEmpty) {
            //println("DEBUG")
            selectedItem = Some(RAIL(pos1, pos2))
            newItemSelected = true
          }
      }
      if (!newItemSelected) selectedItem = None
    }

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
    }
    if (selectedItem.nonEmpty) {
      selectedItem.get match {
        case town : TOWN =>
          gc.strokeRect(town.pos1.x - TOWN_RADIUS * 1.5, town.pos1.y - TOWN_RADIUS * 1.5, TOWN_RADIUS * 3, TOWN_RADIUS * 3)
        case rail : RAIL =>
          gc.lineWidth = 10
          gc.stroke = Color.Gold
          gc.strokeLine(rail.pos1.x, rail.pos1.y, rail.pos2.x, rail.pos2.y)
      }
    }
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
        }
    }
  }
}
