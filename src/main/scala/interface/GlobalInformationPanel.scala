package interface

import logic.world.World

import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color

object GlobalInformationPanel extends GUIComponent {

  val panel = new VBox()

  val mainLabel = new Label("Global Information\n")
  val timeLabel = new Label()
  val moneyLabel = new Label("money : 0")
  val ticketPriceLabel = new Label("Ticket price per Km : ")
  val warningLabel = new Label()
  val nbTrainLabel = new Label("trains : 0")
  val totalPopulationLabel = new Label

  def make(): VBox = {
    panel.children = List(mainLabel, timeLabel, moneyLabel, ticketPriceLabel, nbTrainLabel, totalPopulationLabel)
    panel.style = "-fx-background-color: cornFlowerBlue"
    panel
  }

  def restart(): Unit = {

  }

  def update(): Unit = {
    timeLabel.text = World.timer.time()
    moneyLabel.text = "Money : " + World.company.money.toInt
    ticketPriceLabel.text = "Ticket price per Km : " + World.company.ticketPricePerKm
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
