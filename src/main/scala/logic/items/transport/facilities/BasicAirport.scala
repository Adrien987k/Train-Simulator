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

  override def propertyPane(): Node = {
    val panel = new VBox()
    val airportLabel = new Label("=== Airport ===")
    val capacityLabel = new Label("Capacity : " + capacity)
    val planesLabel = new Label("Planes : " + availableVehicles)
    val waitingPassengerLabel = new Label("Waiting passenger : " + nbWaitingPassengers())
    panel.children = List(airportLabel, capacityLabel, planesLabel, waitingPassengerLabel)
    panel
  }

}