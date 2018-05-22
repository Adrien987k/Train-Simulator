package logic.items.transport.vehicules.components

import logic.economy.Resources.{BOXED, DRY_BULK, LIQUID}
import logic.economy.Resources.ResourceType
import logic.items.transport.vehicules.VehicleTypes._
import logic.items.transport.vehicules.components.TrainComponentTypes._
import logic.world.Company

import scala.collection.mutable.ListBuffer

object TrainComponentFactory {

  def makeEngine(engineType : EngineType, company : Company) : Engine = {
    engineType match {
      case DIESEL_ENGINE =>
        new DieselEngine(engineType, company,
          EngineEvolutionPlan(
            List(5.0, 6.0, 7.0),
            List(3000.0, 4000.0, 5000.0),
            List(100000.0, 100000.0, 120000.0),
            List(50000.0, 60000.0, 70000.0)
          ))

      case ELECTRIC_ENGINE =>
        new ElectricEngine(engineType, company,
          EngineEvolutionPlan(
            List(3.0, 4.0, 5.0),
            List(10000.0, 10000.0, 12000.0),
            List(50000.0, 50000.0, 60000.0),
            List(1500.0, 2000.0, 2500.0)
        ))
    }
  }

  def makePassengerCarriage(vehicleType : VehicleType, company : Company) : PassengerCarriage = {
    val plan = CarriageEvolutionPlan(
      List(10.0, 12.0, 14.0),
      List(5000.0, 6000.0),
      List(500, 750, 900))

    new PassengerCarriage(company, plan)
  }

  def makeResourcesCarriages
  (trainType : VehicleType, company : Company) : ListBuffer[ResourceCarriage] = {
    val plan = CarriageEvolutionPlan(
      List(10.0, 12.0, 14.0),
      List(5000.0, 6000.0),
      List(50, 60))

    ListBuffer(
        new ResourceCarriage(DRY_BULK, company, plan),
        new ResourceCarriage(LIQUID, company, plan),
        new ResourceCarriage(BOXED, company, plan)
      )
  }

  def makeResourceCarriage(vehicleType: VehicleType, resourceType : ResourceType, company: Company) : ResourceCarriage = {
    val plan = CarriageEvolutionPlan(
      List(10.0, 12.0, 14.0),
      List(5000.0, 6000.0),
      List(50, 60))

    new ResourceCarriage(resourceType, company, plan)
  }

}
