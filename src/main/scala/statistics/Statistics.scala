package statistics

import game.Game
import interface.GUI
import utils.DateTime

import scala.collection.mutable.ListBuffer
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.scene.text.{Font, FontWeight}

abstract class StatValue {
  def info() : String
  def average(l : ListBuffer[StatValue]) : StatValue
  def sum(v : StatValue) : StatValue
}

case class IntValue(value : Int) extends StatValue {
  override def info() : String = value.toString

  override def average(l : ListBuffer[StatValue]) : StatValue = {
    val sum = l.foldLeft(value)((sum, intValue) => {
      intValue match {
        case IntValue(iv) => sum + iv
        case _ => sum
      }
    })

    IntValue(sum / (l.size + 1))
  }

  override def sum(sv : StatValue) : StatValue = {
    sv match {
      case IntValue(iv) => IntValue(value + iv)
      case _ => IntValue(value)
    }
  }
}

case class DoubleValue(value : Double) extends StatValue {
  override def info() : String = value.toString

  override def average(l : ListBuffer[StatValue]) : StatValue = {
    val sum = l.foldLeft(value)((sum, intValue) => {
      intValue match {
        case DoubleValue(dv) => sum + dv
        case _ => value
      }
    })

    DoubleValue(sum / (l.size + 1))
  }

  override def sum(sv : StatValue) : StatValue = {
    sv match {
      case DoubleValue(dv) => DoubleValue(value + dv)
      case _ => DoubleValue(value)
    }
  }
}

case class StringValue(v : String) extends StatValue {
  override def info() : String = v.toString
  override def average(l : ListBuffer[StatValue]) : StatValue = l.head
  override def sum(v : StatValue) : StatValue = v
}

case class NoValue() extends StatValue {
  override def info() : String = ""
  override def average(l : ListBuffer[StatValue]) : StatValue= l.head
  override def sum(v : StatValue) : StatValue = v
}


class Statistics(title : String) {

  private val NB_EVENT_DISPLAYED = 20
  private val DIALOG_WIDTH = 1200
  private val DIALOG_HEIGHT = 500
  private val FONT_SIZE = 14

  private case class Event
  (time : DateTime,
   name : String,
   value : StatValue) {

  }

  private val events : ListBuffer[Event] = ListBuffer.empty

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

    dialog.dialogPane().setPrefSize(DIALOG_WIDTH, DIALOG_HEIGHT)

    buildHistoric()
    buildAverages()

    dialog.dialogPane().setContent(pane)

    dialog.showAndWait()
  }

  def buildHistoric() : Unit = {
    leftPane.children.clear()

    leftPane.children.add(leftPaneTitle)

    var toDisplayEvents = events.takeRight(NB_EVENT_DISPLAYED)
    toDisplayEvents = toDisplayEvents.reverse

    toDisplayEvents.foreach(event => {
      val sb = new StringBuilder

      sb.append(event.time.time() + " | ")
      sb.append(event.name + " | ")

      event.value match {
        case _ : NoValue =>

        case _ =>
          sb.append(event.value.info())
      }

      val label = new Label(sb.toString())
      label.font = Font.font(null, FontWeight.Bold, FONT_SIZE)

      leftPane.children.add(label)
    })
  }

  def buildAverages() : Unit = {
    rightPane.children.clear()

    rightPane.children.add(rightPaneTitle)

    val eventMap = events.groupBy[String](event => event.name)
    eventMap.foreach((name_events) => {
      val (name, events) = name_events

      val values = events.foldLeft(ListBuffer[StatValue]())((l, event) => {
        l += event.value
        l
      })

      val mean = events.head.value.average(values)

      val sum = values.foldLeft(values.head)((sum, value) => {
        sum.sum(value)
      })

      val sb = new StringBuilder
      sb.append(name)
      sb.append(events.head.value match {
        case _ : NoValue => ""
        case _ => " | MEAN: " + mean.info() + " | SUM: " + sum.info()
      })

      val label = new Label(sb.toString())
      label.font = Font.font(null, FontWeight.Bold, FONT_SIZE)

      rightPane.children.add(label)
    })
  }

  def deactivateAverages() : Unit = {
    pane.right = null
  }

}
