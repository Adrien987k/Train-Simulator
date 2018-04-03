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

class BasicAirport
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town,
 _capacity : Int)
  extends Airport(transportFacilityType, company, town, _capacity) {

  val panel = new VBox()

  val airportLabel = new Label("=== Airport ===")
  val capacityLabel = new Label()
  val planesLabel = new Label()
  val waitingPassengerLabel = new Label()

  val evolveButton = new Button("Evolve : " +  + Shop.evolutionPrice(transportFacilityType, 1) + "$")
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

  labels = List(airportLabel, capacityLabel, planesLabel, waitingPassengerLabel)

  panel.children = labels ++ List(evolveButton)

  styleLabels()

  override def propertyPane(): Node = {
    capacityLabel.text = "Capacity : " + capacity
    planesLabel.text = "Planes : " + availableVehicles
    waitingPassengerLabel.text = "Waiting passenger : " + nbWaitingPassengers()

    panel
  }

}