package logic.items.transport.facilities

import logic.exceptions.AlreadyMaxLevelException
import logic.items.ItemTypes.TransportFacilityType
import logic.world.{Company, Shop}
import logic.world.towns.Town

import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

class BasicStation
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends Station(transportFacilityType, company : Company, town : Town, _capacity) {

  val panel = new VBox()

  val stationLabel = new Label("=== Station ===")
  val capacityLabel = new Label()
  val trainsLabel = new Label()
  val waitingPassengerLabel = new Label()

  val evolveButton = new Button("Evolve : " + Shop.evolutionPrice(transportFacilityType, 1) + "$")
  evolveButton.font = Font.font(null, FontWeight.ExtraBold, 19)
  evolveButton.textFill = Color.Green

  evolveButton.onAction = _ => {
    try {
      evolve()
    } catch {
      case e : AlreadyMaxLevelException =>
        if (!levelIsMax) {
          if (panel.children.contains(evolveButton))
            panel.children.remove(evolveButton)
        }
    }

    evolveButton.text =
      "Evolve : " + Shop.evolutionPrice(transportFacilityType, level) + "$"

  }

  labels = List(stationLabel, capacityLabel, trainsLabel, waitingPassengerLabel)

  panel.children = labels ++ List(evolveButton)

  styleLabels()

  override def propertyPane(): Node = {
    capacityLabel.text = "Capacity : " + capacity
    trainsLabel.text = "Trains : " + availableVehicles
    waitingPassengerLabel.text = "Waiting passenger : " + nbWaitingPassengers()

    panel
  }

}
