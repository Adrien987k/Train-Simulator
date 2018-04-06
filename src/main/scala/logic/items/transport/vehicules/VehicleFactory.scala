package logic.items.transport.vehicules

import logic.economy.ResourcesTypes.BoxedResourceType
import logic.items.ItemTypes._
import logic.items.transport.facilities._
import logic.items.transport.vehicules.components.VehicleComponentFactory
import logic.items.transport.vehicules.components.VehicleComponentTypes._
import logic.world.Company

import scala.collection.mutable.ListBuffer

object VehicleFactory {

  def makeVehicle
    (vehicleType : VehicleType,
     company : Company,
     transportFacility : TransportFacility) : Vehicle = {

    vehicleType match {

      case DIESEL_TRAIN =>
        new BasicTrain(DIESEL_TRAIN, company, transportFacility.asInstanceOf[Station],
          VehicleComponentFactory.makeEngine(DIESEL_ENGINE, company),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)))

      case ELECTRIC_TRAIN =>
        new BasicTrain(ELECTRIC_TRAIN, company, transportFacility.asInstanceOf[Station],
        VehicleComponentFactory.makeEngine(ELECTRIC_ENGINE, company),
        ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)))

      case BOEING =>
        new BasicPlane(BOEING, company, transportFacility.asInstanceOf[Airport],
          VehicleComponentFactory.makeEngine(KEROSENE_ENGINE, company),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)))

      case CONCORDE =>
        new BasicPlane(CONCORDE, company, transportFacility.asInstanceOf[Airport],
          VehicleComponentFactory.makeEngine(KEROSENE_ENGINE, company),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)))

      case TRUCK =>
        new BasicTruck(TRUCK, company, transportFacility.asInstanceOf[GasStation],
          VehicleComponentFactory.makeEngine(DIESEL_ENGINE, company),
          VehicleComponentFactory.makeResourceCarriage[BoxedResourceType](company))

      //TODO make carriages for ships
      case LINER =>
        new BasicShip(LINER, company, transportFacility.asInstanceOf[Harbor],
          VehicleComponentFactory.makeEngine(SHIP_ENGINE, company),
          ListBuffer.empty)

      case CRUISE_BOAT =>
        new BasicShip(CRUISE_BOAT, company, transportFacility.asInstanceOf[Harbor],
          VehicleComponentFactory.makeEngine(SHIP_ENGINE, company),
          ListBuffer.empty)

      case _ => throw new Exception("Not implemented")
    }
  }

}
