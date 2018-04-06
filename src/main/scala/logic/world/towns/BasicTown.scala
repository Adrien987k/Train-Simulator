package logic.world.towns

import logic.items.transport.facilities.TransportFacility
import utils.Pos

import scala.collection.mutable
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
class BasicTown(pos : Pos, name : String) extends Town(pos, name) {

  val mainPanel = new VBox()

  val townPanel = new VBox()

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

  val transportFacilitiesPanels : mutable.HashMap[Option[TransportFacility], Node] = mutable.HashMap.empty
  transportFacilitiesPanels.+=((station, null))
  transportFacilitiesPanels.+=((airport, null))
  transportFacilitiesPanels.+=((harbor, null))
  transportFacilitiesPanels.+=((gasStation, null))

  styleLabels()

  townPanel.children = labels

  mainPanel.children.add(townPanel)

  override def explore() : Unit = {

  }

  override def produce() : Unit = {

  }

  override def propertyPane() : Node = {
    hasStationLabel.text = if (hasStation) "This town has a station" else "This does not have a station"
    populationLabel.text  = "Population : " + population
    propTravelerLabel.text = "Proportion of traveler : " + proportionTraveler
    posLabel.text = "position : " + pos

    transportFacilitiesPanels.foreach(c => {
      if (c._2 != null && mainPanel.children.contains(c._2)) {
        mainPanel.children.remove(c._2)
      }
    })

    if (hasStation)
      transportFacilitiesPanels.update(station, station.get.propertyPane())

    if (hasAirport)
      transportFacilitiesPanels.update(airport, airport.get.propertyPane())

    if (hasHarbor)
      transportFacilitiesPanels.update(harbor, harbor.get.propertyPane())

    if (hasGasStation)
      transportFacilitiesPanels.update(gasStation, gasStation.get.propertyPane())

    transportFacilitiesPanels.foreach(c => {
      if (c._2 != null) {
        mainPanel.children.add(c._2)
      }
    })

    mainPanel
  }

}
