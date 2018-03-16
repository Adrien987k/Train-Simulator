package logic.items.transport.vehicules

import logic.world.{Company, World}
import interface.{GlobalInformationPanel, WorldCanvas}
import logic.items.transport.facilities.Station
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox

class BasicTrain
(company : Company,
 station : Station,
 engine : Engine,
 carriages : ListBuffer[Carriage])
  extends Train(company, engine, carriages, station) {

  override def propertyPane(): Node = {
    val panel = new VBox()
    val speedLabel = new Label("Speed : " + engine.speed)
    //val maxWeightLabel = new Label("Max weight : " + maxWeight)
    //val maxPassengerLabel = new Label("Max passengers : " + passengerCapacity)
    //val nbPassengerLabel = new Label("Passengers : " + nbPassenger)
    val posLabel = new Label("Position : " + pos)
    panel.children = List(speedLabel, posLabel)
    val goalStationLabel = new Label("Goal station : ")
    if (goalTransportFacility.nonEmpty)
      goalStationLabel.text = "Goal station : " + goalTransportFacility.get.town.name
    panel.children.add(goalStationLabel)
    val chooseDestPanel = new Button("Choose destination")
    chooseDestPanel.onAction = (_ : ActionEvent) => {
      World.company.selectTrain(this)
      WorldCanvas.activeDestinationChoice()
    }
    panel.children.add(chooseDestPanel)
    panel
  }

}
