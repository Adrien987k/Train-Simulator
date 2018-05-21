package statistics

import game.Game
import interface.GUI
import utils.DateTime

import scala.collection.mutable.ListBuffer
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, VBox}

trait StatValue {

  def info() : String

}

case class IntValue(v : Int) extends StatValue
{ override def info() : String = v.toString }

case class DoubleValue(v : Double) extends StatValue
{ override def info() : String = v.toString }

case class StringValue(v : String) extends StatValue
{ override def info() : String = v.toString }

case class NoValue() extends StatValue
{ override def info() : String = "" }


class Statistics(title : String) {

  case class Event
  (time : DateTime,
   name : String,
   value : StatValue) {

  }

  val events : ListBuffer[Event] = ListBuffer.empty

  def newEvent(name : String, v : Double) : Unit =
    events += Event(Game.world.time(), name, DoubleValue(v))

  def newEvent(name : String, v : Int) : Unit =
    events += Event(Game.world.time(), name, IntValue(v))

  def newEvent(name : String, v : StatValue) : Unit =
    events += Event(Game.world.time(), name, v)

  def newEvent(name : String, v : String) : Unit =
    events += Event(Game.world.time(), name, StringValue(v))

  def newEvent(name : String) : Unit =
    events += Event(Game.world.time(), name, NoValue())


  private val pane = new BorderPane
  private val rightPane = new VBox
  private val leftPane = new VBox

  private val rightPaneTitle = new Label("=== Averages ===\n")
  private val leftPaneTitle = new Label("=== Historic ===\n")

  rightPane.children.add(rightPaneTitle)
  leftPane.children.add(leftPaneTitle)

  pane.center = leftPane
  pane.right = rightPane

  def show() : Unit = {
    val statsTitle = title

    val dialog = new Alert(AlertType.Information) {
      initOwner(GUI.stage)
      title = statsTitle + " Statistics"
      headerText = "All statistics about " + statsTitle
    }

    dialog.dialogPane().setPrefSize(1000, 500)

    leftPane.children.clear()

    leftPane.children.add(leftPaneTitle)

    var toDisplayEvents = events.takeRight(25)
    toDisplayEvents = toDisplayEvents.reverse

    toDisplayEvents.foreach(event => {
      val sb = new StringBuilder

      sb.append("time: " + event.time.time() + " | ")
      sb.append("event: " + event.name + " | ")

      event.value match {
        case v : NoValue =>

        case _ =>
          sb.append("value: " + event.value.info())
      }

      val label = new Label(sb.toString())

      leftPane.children.add(label)
    })

    dialog.dialogPane().setContent(pane)

    dialog.showAndWait()
  }

}
