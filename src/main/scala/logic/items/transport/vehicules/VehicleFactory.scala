package logic.items.transport.vehicules

import logic.items.transport.facilities._
import logic.items.transport.vehicules.VehicleTypes._
import logic.items.transport.vehicules.components.TrainComponentFactory
import logic.items.transport.vehicules.components.TrainComponentTypes._
import logic.world.Company

import scala.collection.mutable.ListBuffer

object VehicleFactory {

  def make
    (vehicleType : VehicleType,
     company : Company,
     transportFacility : TransportFacility) : Vehicle = {

    vehicleType match {

      case DIESEL_TRAIN =>
        new Train(DIESEL_TRAIN, company,
          TrainComponentFactory.makeEngine(DIESEL_ENGINE, company),
          ListBuffer(TrainComponentFactory.makePassengerCarriage(vehicleType, company))
            ++ TrainComponentFactory.makeResourcesCarriages(vehicleType, company),
          transportFacility.asInstanceOf[Station],
          TrainEvolutionPlan(List(20000.0)))

      case ELECTRIC_TRAIN =>
        new Train(ELECTRIC_TRAIN, company,
        TrainComponentFactory.makeEngine(ELECTRIC_ENGINE, company),
        ListBuffer(TrainComponentFactory.makePassengerCarriage(vehicleType, company))
          ++ TrainComponentFactory.makeResourcesCarriages(vehicleType, company),
        transportFacility.asInstanceOf[Station],
          TrainEvolutionPlan(List(15000.0)))

      case BOEING =>
        new Plane(BOEING, company, transportFacility.asInstanceOf[Airport],
          PlaneEvolutionPlan(
            List(1000000.0),
            List(10.0),
            List(12.0),
            List(1000.0),
            List(30000.0)
          )
        )

      case CONCORDE =>
        new Plane(CONCORDE, company, transportFacility.asInstanceOf[Airport],
          PlaneEvolutionPlan(
            List(500000.0),
            List(15.0),
            List(17.0),
            List(350.0),
            List(50000.0)
          )
        )

      case TRUCK =>
        new Truck(TRUCK, company, transportFacility.asInstanceOf[GasStation],
          TruckEvolutionPlan(
            List(5.0),
            List(5000.0),
            List(3000.0)
          )
        )

      case LINER =>
        new Ship(LINER, company, transportFacility.asInstanceOf[Harbor],
          ShipEvolutionPlan(
            List(2000000.0),
            List(4.0),
            List(5000000.0),
            List(0.0),
            List(50000.0),
            List(150.0)
          )
        )

      case CRUISE_BOAT =>
        new Ship(CRUISE_BOAT, company, transportFacility.asInstanceOf[Harbor],
          ShipEvolutionPlan(
            List(1500000.0),
            List(6.0),
            List(2000000.0),
            List(3000.0),
            List(30000.0),
            List(0.0)
          )
        )

      case _ => throw new Exception("Not implemented")
    }
  }

}
