package interface

import java.text.SimpleDateFormat

import engine.World

import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color

object GlobalInformationPanel extends GUIComponent {

  val panel = new VBox()

  val mainLabel = new Label("Global Information\n")
  val timeLabel = new Label()
  val moneyLabel = new Label("money : 0")
  val warningLabel = new Label()
  val nbTrainLabel = new Label("trains : 0")
  val totalPopulationLabel = new Label

  def make(): VBox = {
    panel.children = List(mainLabel, timeLabel, moneyLabel, nbTrainLabel, totalPopulationLabel)
    panel
  }

  def update(time: Long): Unit = {
    val hourFormat = new SimpleDateFormat("HH:mm")
    timeLabel.text = hourFormat.format(time / (1e3 * 4))
    moneyLabel.text = "Money : " + World.company.money.toInt
    nbTrainLabel.text = "Trains : " + World.company.trains.size
    totalPopulationLabel.text = "Population : " + World.totalPopulation
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
