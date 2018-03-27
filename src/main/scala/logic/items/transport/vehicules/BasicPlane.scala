package logic.items.transport.vehicules

import game.Game
import logic.world.Company
import interface.WorldCanvas
import logic.items.transport.facilities.{Airport, Station}

import scala.collection.mutable.ListBuffer
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox

class BasicPlane
(company : Company,
 airport  : Airport,
 engine : Engine,
 carriages : ListBuffer[Carriage])
  extends Plane(company, engine, carriages, airport) {

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
    //TODO
    //chooseDestPanel.onAction = (_ : ActionEvent) => {
      //Game.world.company.selectTrain(this)
      //WorldCanvas.activeDestinationChoice()
    //}
    panel.children.add(chooseDestPanel)
    panel
  }

}
