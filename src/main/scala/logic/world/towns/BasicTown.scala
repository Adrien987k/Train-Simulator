package logic.world.towns

import logic._
import logic.exceptions.CannotBuildItemException
import logic.items.transport.facilities.{BasicStation, Station}
import logic.world.World
import utils.Pos

import scala.util.Random
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, VBox}

class BasicTown(pos : Pos, name : String) extends Town(pos : Pos, name : String) {

  override def explore(): Unit = {

  }

  override def produce(): Unit = {

  }

  override def propertyPane(): Node = {
    val mainPane = new BorderPane
    val panel = new VBox()
    val nameLabel = new Label("=== " + name + " ===")
    val hasStationLabel = new Label(if (hasStation) "This town has a station"
    else "This does not have a station")
    val populationLabel = new Label("Population : " + population)
    val propTravelerLabel = new Label("Proportion of traveler : " + proportionTraveler)
    val posLabel = new Label("position : " + pos)
    panel.children = List(nameLabel, hasStationLabel, populationLabel, propTravelerLabel, posLabel, new Label("\n"))
    mainPane.top = panel
    if (hasStation)
      mainPane.center = station.get.propertyPane()
    mainPane
  }

}
