package logic.items.transport.facilities

import logic.items.ItemTypes.TransportFacilityType
import logic.world.Company
import logic.world.towns.Town

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicAirport
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town)
  extends Airport(transportFacilityType, company : Company, town : Town) {

  val panel = new VBox()

  val airportLabel = new Label("=== Airport ===")
  val capacityLabel = new Label()
  val planesLabel = new Label()
  val waitingPassengerLabel = new Label()

  labels = List(airportLabel, capacityLabel, planesLabel, waitingPassengerLabel)

  panel.children = labels

  styleLabels()

  override def propertyPane(): Node = {
    capacityLabel.text = "Capacity : " + capacity
    planesLabel.text = "Planes : " + availableVehicles
    waitingPassengerLabel.text = "Waiting passenger : " + nbWaitingPassengers()

    panel
  }

}