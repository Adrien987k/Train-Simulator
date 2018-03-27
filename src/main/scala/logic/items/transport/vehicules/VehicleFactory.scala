package logic.items.transport.vehicules

import logic.economy.ResourcesTypes
import logic.items.ItemTypes.{BOEING, DIESEL_TRAIN, ELECTRIC_TRAIN, VehicleType}
import logic.items.transport.facilities.{Airport, Station, TransportFacility}
import logic.items.transport.vehicules.VehicleComponentType._
import logic.world.Company

import scala.collection.mutable.ListBuffer

private object VehicleComponentFactory {

  def makeEngine(engineType : VehicleComponentType.EngineType) : Engine = {
    engineType match {
      case DIESEL_ENGINE => new DieselEngine(5.0, 20000.0, 100000.0)
      case ELECTRIC_ENGINE => new ElectricEngine(3.0, 10000.0, 50000.0)
    }
  }

  def makePassengerCarriage() : Carriage = {
    new PassengerCarriage(5.0, 1000.0, 500)
  }

  def makeResourceCarriage[R <: ResourcesTypes.ResourceType]
    (resourceType : ResourcesTypes.ResourceType) : ResourceCarriage[R] = {
    new ResourceCarriage[R](5.0, 5000.0)
  }

}

object VehicleFactory {

  def makeVehicle
    (vehicleType : VehicleType,
     company : Company,
     transportFacility : TransportFacility) : Vehicle = {

    vehicleType match {

      case DIESEL_TRAIN =>
        new BasicTrain(company, transportFacility.asInstanceOf[Station],
          VehicleComponentFactory.makeEngine(DIESEL_ENGINE),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage()))

      case ELECTRIC_TRAIN =>
        new BasicTrain(company, transportFacility.asInstanceOf[Station],
        VehicleComponentFactory.makeEngine(ELECTRIC_ENGINE),
        ListBuffer(VehicleComponentFactory.makePassengerCarriage()))

        //TODO Add boeing + concorde + plane engine and carriages
        /*
      case BOEING =>
        new BasicPlane(company, transportFacility.asInstanceOf[Airport],
          VehicleComponentFactory.makeEngine()) */
    }
  }

}
