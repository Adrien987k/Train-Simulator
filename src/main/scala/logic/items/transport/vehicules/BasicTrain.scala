package logic.items.transport.vehicules

import game.Game
import logic.world.Company
import interface.WorldCanvas
import logic.items.ItemTypes.TrainType
import logic.items.transport.facilities.Station

import scalafx.Includes._
import scala.collection.mutable.ListBuffer
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox

class BasicTrain
(trainType : TrainType,
 company : Company,
 station : Station,
 engine : Engine,
 carriages : ListBuffer[Carriage])
  extends Train(trainType, company, engine, carriages, station) {

  override def propertyPane(): Node = {
    val panel = new VBox()
    val typeLabel = new Label(trainType.name)
    val speedLabel = new Label("Max Speed : " + engine.maxSpeed)
    val maxPassengerLabel = new Label("Passengers capacity : " + passengerCapacity)
    val nbPassengerLabel = new Label("Passengers : " + nbPassenger)
    val posLabel = new Label("Position : " + pos)
    panel.children = List(typeLabel, speedLabel, posLabel, maxPassengerLabel, nbPassengerLabel)
    val goalStationLabel = new Label("Goal station : ")
    if (goalTransportFacility.nonEmpty) {
      goalStationLabel.text = "Goal station : " + goalTransportFacility.get.town.name
      panel.children.add(goalStationLabel)
    }
    val chooseDestPanel = new Button("Choose destination")
    chooseDestPanel.onAction = (_ : ActionEvent) => {
      Game.world.company.selectVehicle(this)
      WorldCanvas.activeDestinationChoice()
    }
    panel.children.add(chooseDestPanel)
    panel
  }

}
