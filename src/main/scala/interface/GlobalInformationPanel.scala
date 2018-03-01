package interface

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import engine.World

import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color

object GlobalInformationPanel {

  val mainLabel = new Label("Global Information\n")
  val timeLabel = new Label()
  val moneyLabel = new Label("money : 0")
  val warningLabel = new Label()
  val panel = new VBox()

  val nbTrainLabel = new Label("trains : 0")
  def makeGlobalInfoPanel(): VBox = {
    panel.children = List(mainLabel, timeLabel, moneyLabel, nbTrainLabel)
    panel
  }

  def update(time: Long): Unit = {
    val hourFormat = new SimpleDateFormat("HH:mm")
    timeLabel.text = hourFormat.format(time / (1e3 * 4))
    moneyLabel.text = "money : " + World.company.money
    nbTrainLabel.text = "trains : " + World.company.trains.size
  }

  def displayWarningMessage(message : String): Unit = {
    warningLabel.textFill = Color.Red
    warningLabel.text = message
    if (!panel.children.contains(warningLabel))
      panel.children.add(warningLabel)
  }

  def removeWarningMessage(): Unit = {
    if (panel.children.contains(warningLabel))
      panel.children.remove(warningLabel)
  }

}
