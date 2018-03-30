package logic.items.transport.facilities

import logic.items.ItemTypes.TransportFacilityType
import logic.world.Company
import logic.world.towns.Town

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class BasicStation
(override val transportFacilityType : TransportFacilityType,
 override val company : Company,
 override val town : Town)
  extends Station(transportFacilityType, company : Company, town : Town) {

  val panel = new VBox()

  val stationLabel = new Label("=== Station ===")
  val capacityLabel = new Label()
  val trainsLabel = new Label()
  val waitingPassengerLabel = new Label()

  labels = List(stationLabel, capacityLabel, trainsLabel, waitingPassengerLabel)

  panel.children = labels

  styleLabels()

  override def propertyPane(): Node = {
    capacityLabel.text = "Capacity : " + capacity
    trainsLabel.text = "Trains : " + availableVehicles
    waitingPassengerLabel.text = "Waiting passenger : " + nbWaitingPassengers()

    panel
  }

}
