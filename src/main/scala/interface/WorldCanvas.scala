package interface

import engine.World
import link.{Change, Observer}
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.control.ScrollPane
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color

object WorldCanvas extends Observer {

  val canvas = new Canvas(World.MAP_WIDTH, World.MAP_HEIGHT)
  val gc: GraphicsContext = canvas.graphicsContext2D

  canvas.onMouseClicked = (event : MouseEvent) => {

  }

  def makeWorldCanvas(): Node = {
    val scrollPane = new ScrollPane
    scrollPane.content = canvas
    scrollPane
  }

  def initWorld(townsPositions : List[Pos]): Unit = {
    gc.fill = Color.Red
    townsPositions.foreach(pos => {
      gc.fillOval(pos.x, pos.y, 20, 20)
    })
  }

  override def notifyChange(changes: ListBuffer[Change]): Unit = {
    //TODO Takes changes into account + modify class change
  }
}
