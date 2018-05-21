package interface

import game.Game

import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

object GlobalInformationPanel extends GUIComponent {

  private val panel = new VBox()

  private val mainLabel = new Label("Global Information\n")
  private val timeLabel = new Label()
  private val moneyLabel = new Label("money : 0")
  private val ticketPriceLabel = new Label("Ticket price per Km : ")
  private val warningLabel = new Label()
  private val nbVehicleLabel = new Label("Total vehicles : 0")
  private val totalPopulationLabel = new Label

  private val statsButton = new Button("Company statistics")
  private val contractsButton = new Button("Contracts")

  private val labels : List[Label] =
    List(mainLabel, timeLabel, moneyLabel, ticketPriceLabel, nbVehicleLabel, totalPopulationLabel)

  def make() : VBox = {
    panel.children = labels ++ List(statsButton, contractsButton)

    labels.foreach(_.font = Font.font(null, FontWeight.Bold, 18))
    warningLabel.font = Font.font(null, FontWeight.ExtraBold, 18)
    statsButton.font = Font.font(null, FontWeight.ExtraBold, 18)
    contractsButton.font = Font.font(null, FontWeight.ExtraBold, 18)

    statsButton.onAction = _ => Game.world.company.showStatistics()
    contractsButton.onAction = _ => Game.world.company.showContractStatistics()

    panel.style = "-fx-background-color: cornFlowerBlue"
    panel
  }

  def restart() : Unit = {
    warningLabel.text = ""
  }

  def update() : Unit = {
    timeLabel.text = Game.world.time().time()
    moneyLabel.text = "Money : " + Game.world.company.money.toInt
    ticketPriceLabel.text = "Ticket price per Km : " + Game.world.company.ticketPricePerKm
    nbVehicleLabel.text = "Total vehicles  : " + Game.world.company.vehicles.size
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
