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
          TrainEvolutionPlan(List(20000.0, 25000.0, 30000.0)))

      case ELECTRIC_TRAIN =>
        new Train(ELECTRIC_TRAIN, company,
        TrainComponentFactory.makeEngine(ELECTRIC_ENGINE, company),
        ListBuffer(TrainComponentFactory.makePassengerCarriage(vehicleType, company))
          ++ TrainComponentFactory.makeResourcesCarriages(vehicleType, company),
        transportFacility.asInstanceOf[Station],
          TrainEvolutionPlan(List(15000.0, 16000.0, 16000.0)))

      case BOEING =>
        new Plane(BOEING, company, transportFacility.asInstanceOf[Airport],
          PlaneEvolutionPlan(
            List(1000000.0, 1100000.0, 1300000.0, 1500000.0),
            List(10.0, 11.0, 12.0, 13.0, 15.0),
            List(12.0, 13.0, 14.0, 15.0, 16.0),
            List(1000.0, 1500.0, 1600.0, 1800.0),
            List(30000.0, 35000.0, 40000.0, 50000.0)
          )
        )

      case CONCORDE =>
        new Plane(CONCORDE, company, transportFacility.asInstanceOf[Airport],
          PlaneEvolutionPlan(
            List(500000.0, 600000.0, 700000.0, 750000.0),
            List(15.0, 16.0, 16.5, 17.0, 18.0),
            List(17.0, 17.0, 17.5, 18.0, 18.5),
            List(350.0, 450.0, 550.0, 700.0, 1000.0),
            List(50000.0, 60000.0, 70000.0, 80000.0)
          )
        )

      case TRUCK =>
        new Truck(TRUCK, company, transportFacility.asInstanceOf[GasStation],
          TruckEvolutionPlan(
            List(5.0, 6.0, 7.0),
            List(5000.0, 10000.0, 15000.0),
            List(3000.0, 5000.0, 10000.0)
          )
        )

      case LINER =>
        new Ship(LINER, company, transportFacility.asInstanceOf[Harbor],
          ShipEvolutionPlan(
            List(2000000.0, 2200000.0, 2400000.0, 3000000.0),
            List(4.0, 4.5, 5.5, 7.0),
            List(5000000.0, 6000000.0, 7500000.0, 10000000.0),
            List(0.0),
            List(50000.0, 60000.0, 70000.0, 90000.0),
            List(150.0, 300.0, 450.0, 650.0)
          )
        )

      case CRUISE_BOAT =>
        new Ship(CRUISE_BOAT, company, transportFacility.asInstanceOf[Harbor],
          ShipEvolutionPlan(
            List(1500000.0, 1600000.0, 1700000.0, 1800000.0),
            List(6.0, 7.0, 7.5, 8.0),
            List(2000000.0, 2500000.0, 3000000.0, 3500000.0),
            List(3000.0, 4000.0, 6000.0, 7000.0),
            List(30000.0, 40000.0, 55000.0, 75000.0),
            List(0.0)
          )
        )

      case _ => throw new Exception("Not implemented")
    }
  }

}
