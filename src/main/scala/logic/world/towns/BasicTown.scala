package logic.world.towns

import utils.Pos

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, VBox}

class BasicTown(pos : Pos, name : String) extends Town(pos, name) {

  val mainPane = new BorderPane

  val panel = new VBox()

  val nameLabel = new Label("=== " + name + " ===")
  val hasStationLabel = new Label()
  val populationLabel = new Label()
  val propTravelerLabel = new Label()
  val posLabel = new Label()

  labels = List(nameLabel,
    hasStationLabel,
    populationLabel,
    propTravelerLabel,
    posLabel,
    new Label("\n"))

  styleLabels()

  panel.children = labels

  mainPane.top = panel

  override def explore(): Unit = {

  }

  override def produce(): Unit = {

  }

  override def propertyPane() : Node = {
    hasStationLabel.text = if (hasStation) "This town has a station" else "This does not have a station"
    populationLabel.text  = "Population : " + population
    propTravelerLabel.text = "Proportion of traveler : " + proportionTraveler
    posLabel.text = "position : " + pos

    if (hasStation)
      mainPane.center = station.get.propertyPane()

    if (hasAirport)
      mainPane.bottom = airport.get.propertyPane()

    mainPane
  }

}
