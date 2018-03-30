package logic.items.transport.vehicules

import logic.economy.ResourcesTypes
import logic.items.ItemTypes._
import logic.items.transport.facilities.{Airport, Station, TransportFacility}
import logic.items.transport.vehicules.VehicleComponentType._
import logic.world.Company

import scala.collection.mutable.ListBuffer

private object VehicleComponentFactory {

  def makeEngine(engineType : VehicleComponentType.EngineType) : Engine = {
    engineType match {
      case DIESEL_ENGINE => new DieselEngine(5.0, 20000.0, 100000.0)
      case ELECTRIC_ENGINE => new ElectricEngine(3.0, 10000.0, 50000.0)
      case KEROSENE_ENGINE => new KeroseneEngine(15.0, 30000.0, 100000.0)
    }
  }

  def makePassengerCarriage(maxSpeed : Double, maxWeight : Double, maxCapacity : Int) : Carriage = {
    new PassengerCarriage(maxSpeed, maxWeight, maxCapacity)
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
        new BasicTrain(DIESEL_TRAIN, company, transportFacility.asInstanceOf[Station],
          VehicleComponentFactory.makeEngine(DIESEL_ENGINE),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(5.0, 1000.0, 500)))

      case ELECTRIC_TRAIN =>
        new BasicTrain(ELECTRIC_TRAIN, company, transportFacility.asInstanceOf[Station],
        VehicleComponentFactory.makeEngine(ELECTRIC_ENGINE),
        ListBuffer(VehicleComponentFactory.makePassengerCarriage(5.0, 700.0, 800)))

      case BOEING =>
        new BasicPlane(BOEING, company, transportFacility.asInstanceOf[Airport],
          VehicleComponentFactory.makeEngine(KEROSENE_ENGINE),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(15.0, 2500.0, 300)))

      case CONCORDE =>
        new BasicPlane(CONCORDE, company, transportFacility.asInstanceOf[Airport],
          VehicleComponentFactory.makeEngine(KEROSENE_ENGINE),
          ListBuffer(VehicleComponentFactory.makePassengerCarriage(20.0, 1500.0, 100)))
    }
  }

}
