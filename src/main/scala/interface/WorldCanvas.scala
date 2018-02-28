package interface

import engine.{CannotBuildItemException, ItemType, World}
import link.{Change, CreationChange, Observer}
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.Includes._
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

  val canvas = new Canvas(World.MAP_WIDTH, World.MAP_HEIGHT)
  val gc: GraphicsContext = canvas.graphicsContext2D
  var items: ListBuffer[Item] = ListBuffer.empty

  def makeWorldCanvas(): Node = {
    val scrollPane = new ScrollPane
    scrollPane.content = canvas
    scrollPane
  }

  def initWorld(townsPositions : List[Pos]): Unit = {
    canvas.onMouseClicked = (event : MouseEvent) => {
      try {
        if (ItemsButtonBar.selected != null)
          ItemsButtonBar.selected.foreach(World.company.tryPlace(_, new Pos(event.x.toInt, event.y.toInt)))
      } catch {
        case e: CannotBuildItemException =>
          println("EXCEPTION " + e.getMessage)
      }
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
    items.foreach {
      case town: TOWN =>
        gc.fill = if (town.level == 0) Color.Red else Color.Green
        gc.fillOval(town.pos1.x - TOWN_RADIUS, town.pos1.y - TOWN_RADIUS, TOWN_RADIUS * 2, TOWN_RADIUS * 2)
      case rail: RAIL =>
        gc.fill = Color.Black
        gc.stroke = 20
        gc.strokeLine(rail.pos1.x, rail.pos1.y, rail.pos2.x, rail.pos2.y)
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
