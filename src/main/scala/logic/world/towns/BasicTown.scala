package logic.world.towns

import utils.Pos

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, VBox}
class BasicTown(pos : Pos, name : String) extends Town(pos, name) {

  val mainPanel = new BorderPane

  val townPanel = new VBox()

  val nameLabel = new Label("=== " + name + " ===")
  val populationLabel = new Label()
  val propTravelerLabel = new Label()
  val posLabel = new Label()

  labels = List(nameLabel,
    populationLabel,
    propTravelerLabel,
    posLabel,
    new Label("\n"))

  styleLabels()

  townPanel.children = labels

  mainPanel.top = townPanel

  override def explore() : Unit = {

  }

  override def produce() : Unit = {

  }

  val transportFacilitiesVBox = new VBox()

  mainPanel.center = transportFacilitiesVBox

  override def propertyPane() : Node = {
    populationLabel.text  = "Population : " + population
    propTravelerLabel.text = "Proportion of traveler : " + proportionTraveler
    posLabel.text = "position : " + pos

    if (hasStation && !transportFacilitiesVBox.children.contains(station.get.propertyPane()))
      transportFacilitiesVBox.children.add(station.get.propertyPane())

    if (hasAirport && !transportFacilitiesVBox.children.contains(airport.get.propertyPane()))
      transportFacilitiesVBox.children.add(airport.get.propertyPane())

    if (hasHarbor && !transportFacilitiesVBox.children.contains(harbor.get.propertyPane()))
      transportFacilitiesVBox.children.add(harbor.get.propertyPane())

    if (hasGasStation && !transportFacilitiesVBox.children.contains(gasStation.get.propertyPane()))
      transportFacilitiesVBox.children.add(gasStation.get.propertyPane())

    mainPanel
  }

}
