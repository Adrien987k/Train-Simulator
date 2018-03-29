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

  override def propertyPane(): Node = {
    val panel = new VBox()
    val stationLabel = new Label("=== Station ===")
    val capacityLabel = new Label("Capacity : " + capacity)
    val trainsLabel = new Label("Trains : " + availableVehicles)
    val waitingPassengerLabel = new Label("Waiting passenger : " + nbWaitingPassengers())
    panel.children = List(stationLabel, capacityLabel, trainsLabel, waitingPassengerLabel)
    panel
  }

}
