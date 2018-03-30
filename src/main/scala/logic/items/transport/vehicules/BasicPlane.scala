package logic.items.transport.vehicules

import game.Game
import logic.world.Company
import interface.WorldCanvas
import logic.items.ItemTypes.PlaneType
import logic.items.transport.facilities.Airport

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.text.{Font, FontWeight}

class BasicPlane
(planeType : PlaneType,
 company : Company,
 airport  : Airport,
 engine : Engine,
 carriages : ListBuffer[Carriage])
  extends Plane(planeType, company, engine, carriages, airport) {

  val panel = new VBox()

  val typeLabel = new Label(planeType.name)
  val speedLabel = new Label()
  val maxPassengerLabel = new Label()
  val nbPassengerLabel = new Label()
  val posLabel = new Label()
  val goalStationLabel = new Label()

  labels = List(typeLabel,
  speedLabel,
  maxPassengerLabel,
  nbPassengerLabel,
  posLabel,
  goalStationLabel)

  panel.children = labels

  styleLabels()

  val chooseDestPanel = new Button("Choose destination")

  override def propertyPane() : Node = {
    typeLabel.text = planeType.name
    speedLabel.text = "Max Speed : " + engine.maxSpeed
    maxPassengerLabel.text = "Max passengers : " + passengerCapacity
    nbPassengerLabel.text = "Passengers : " + nbPassenger
    posLabel.text = "Position : " + pos

    if (goalTransportFacility.nonEmpty) {
      goalStationLabel.text = "Goal airport : " + goalTransportFacility.get.town.name

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
