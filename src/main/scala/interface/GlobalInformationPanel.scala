package interface

import game.Game

import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

object GlobalInformationPanel extends GUIComponent {

  val panel = new VBox()

  val mainLabel = new Label("Global Information\n")
  val timeLabel = new Label()
  val moneyLabel = new Label("money : 0")
  val ticketPriceLabel = new Label("Ticket price per Km : ")
  val warningLabel = new Label()
  val nbTrainLabel = new Label("Total vehicles : 0")
  val totalPopulationLabel = new Label

  val labels : List[Label] = List(mainLabel, timeLabel, moneyLabel, ticketPriceLabel, nbTrainLabel, totalPopulationLabel)

  def make() : VBox = {
    panel.children = labels

    labels.foreach(_.font = Font.font(null, FontWeight.Bold, 18))
    warningLabel.font = Font.font(null, FontWeight.ExtraBold, 18)

    panel.style = "-fx-background-color: cornFlowerBlue"
    panel
  }

  def restart() : Unit = {
    warningLabel.text = ""
  }

  def update() : Unit = {
    timeLabel.text = Game.world.gameDateTime.time()
    moneyLabel.text = "Money : " + Game.world.company.money.toInt
    ticketPriceLabel.text = "Ticket price per Km : " + Game.world.company.ticketPricePerKm
    nbTrainLabel.text = "Trains : " + Game.world.company.vehicles.size
    totalPopulationLabel.text = "Population : " + Game.world.totalPopulation
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
