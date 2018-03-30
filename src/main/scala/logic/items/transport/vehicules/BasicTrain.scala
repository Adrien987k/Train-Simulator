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
import scalafx.scene.text.{Font, FontWeight}

class BasicTrain
(trainType : TrainType,
 company : Company,
 station : Station,
 engine : Engine,
 carriages : ListBuffer[Carriage])
  extends Train(trainType, company, engine, carriages, station) {

  val panel = new VBox()

  val typeLabel = new Label(trainType.name)
  val speedLabel = new Label()
  val maxPassengerLabel = new Label()
  val nbPassengerLabel = new Label()
  val goalStationLabel = new Label()
  val posLabel = new Label()

  labels = List(typeLabel,
  speedLabel,
  posLabel,
  maxPassengerLabel,
  nbPassengerLabel,
  goalStationLabel)

  panel.children = labels

  styleLabels()

  val chooseDestPanel = new Button("Choose destination")

  override def propertyPane(): Node = {
    speedLabel.text = "Max Speed : " + engine.maxSpeed
    maxPassengerLabel.text = "Passengers capacity : " + passengerCapacity
    nbPassengerLabel.text = "Passengers : " + nbPassenger
    posLabel.text = "Position : " + pos

    if (goalTransportFacility.nonEmpty) {
      goalStationLabel.text = "Goal station : " + goalTransportFacility.get.town.name

      if (!panel.children.contains(goalStationLabel))
        panel.children.add(goalStationLabel)
    }

    chooseDestPanel.font = Font.font(null, FontWeight.Bold, 18)

    chooseDestPanel.onAction = _ => {
      Game.world.company.selectVehicle(this)
      WorldCanvas.activeDestinationChoice()
    }

    if (!panel.children.contains(chooseDestPanel))
      panel.children.add(chooseDestPanel)

    panel
  }

}
