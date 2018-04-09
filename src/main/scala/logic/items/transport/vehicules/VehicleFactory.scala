package logic.items.transport.vehicules

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
        new Train(DIESEL_TRAIN, company,
          VehicleComponentFactory.makeEngine(DIESEL_ENGINE, company),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)),
          transportFacility.asInstanceOf[Station])

      case ELECTRIC_TRAIN =>
        new Train(ELECTRIC_TRAIN, company,
        VehicleComponentFactory.makeEngine(ELECTRIC_ENGINE, company),
        ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)),
        transportFacility.asInstanceOf[Station])

      case BOEING =>
        new Plane(BOEING, company,
          VehicleComponentFactory.makeEngine(KEROSENE_ENGINE, company),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)),
          transportFacility.asInstanceOf[Airport])

      case CONCORDE =>
        new Plane(CONCORDE, company,
          VehicleComponentFactory.makeEngine(KEROSENE_ENGINE, company),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(vehicleType, company)),
          transportFacility.asInstanceOf[Airport])

      case TRUCK =>
        new Truck(TRUCK, company,
          VehicleComponentFactory.makeEngine(DIESEL_ENGINE, company),
          VehicleComponentFactory.makeResourcesCarriages(company).head,
          transportFacility.asInstanceOf[GasStation])

      //TODO make carriages for ships
      case LINER =>
        new Ship(LINER, company,
          VehicleComponentFactory.makeEngine(SHIP_ENGINE, company),
          ListBuffer.empty,
          transportFacility.asInstanceOf[Harbor])

      case CRUISE_BOAT =>
        new Ship(CRUISE_BOAT, company,
          VehicleComponentFactory.makeEngine(SHIP_ENGINE, company),
          ListBuffer.empty,
          transportFacility.asInstanceOf[Harbor])

      case _ => throw new Exception("Not implemented")
    }
  }

}
