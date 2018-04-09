package logic.items.transport.vehicules.components

import logic.economy.Resources.{DRY_BULK, ResourceType}
import logic.exceptions.AlreadyMaxLevelException
import logic.items.ItemTypes._
import logic.items.transport.vehicules.components.VehicleComponentTypes._
import logic.world.Company

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object VehicleComponentFactory {

  val evolutions : mutable.HashMap[VehicleComponentType, Map[Int, List[Double]]] = mutable.HashMap.empty

  evolutions += (DIESEL_ENGINE -> Map(
    1 -> List(5.0, 3000.0, 100000.0, 50000.0),
    2 -> List(6.0, 4000.0, 100000.0, 60000.0),
    3 -> List(7.0, 5000.0, 120000.0, 70000.0)
  ))

  evolutions += (ELECTRIC_ENGINE -> Map(
    1 -> List(3.0, 10000.0, 50000.0, 1500.0),
    2 -> List(4.0, 10000.0, 50000.0, 2000.0),
    3 -> List(5.0, 12000.0, 60000.0, 2500.0)
  ))

  evolutions += (KEROSENE_ENGINE -> Map(
    1 -> List(15.0, 30000.0, 100000.0, 2000000.0),
    2 -> List(20.0, 50000.0, 150000.0, 3000000.0),
    3 -> List(25.0, 75000.0, 200000.0, 3000000.0)
  ))

  evolutions += (SHIP_ENGINE -> Map(
    1 -> List(2.0, 100000.0, 1000000.0, 5000000.0),
    2 -> List(3.0, 150000.0, 1500000.0, 7500000.0),
    3 -> List(4.0, 200000.0, 2000000.0, 10000000.0)
  ))

  evolutions += (TRAIN_PASSENGER_CARRIAGE -> Map(
    1 -> List(10.0, 1000.0, 500),
    2 -> List(12.0, 1200.0, 750),
    3 -> List(14.0, 1400.0, 900)
  ))

  evolutions += (CONCORDE_PASSENGER_CARRIAGE -> Map(
    1 -> List(30.0, 5000.0, 100),
    2 -> List(30.0, 6000.0, 120),
    3 -> List(35.0, 7000.0, 150)
  ))

  evolutions += (BOEING_PASSENGER_CARRIAGE -> Map(
    1 -> List(30.0, 2500.0, 300),
    2 -> List(30.0, 3500.0, 500),
    3 -> List(30.0, 4000.0, 800)
  ))

  def evolve(vehicleComponent : VehicleComponent) : Unit = {
    val statsOpt = getStatsList(vehicleComponent.vehicleComponentType, vehicleComponent.level + 1)
    val statsLevelSupOpt = getStatsList(vehicleComponent.vehicleComponentType, vehicleComponent.level + 2)

    val stats = statsOpt.get

    vehicleComponent.level += 1

    vehicleComponent.vehicleComponentType match {
      case _ : EngineType => evolveEngine(vehicleComponent.asInstanceOf[Engine], stats)

      case _ : PassengerCarriageType => evolvePassengerCarriage(vehicleComponent.asInstanceOf[PassengerCarriage], stats)
    }

    if (statsLevelSupOpt.isEmpty)
      throw new AlreadyMaxLevelException(
        vehicleComponent.vehicleComponentType.name + " is level max")
  }

  private def evolveEngine(engine : Engine, stats : List[Double]) : Unit = {
    engine.evolve(stats.head, stats(1), stats(2), stats(3))
  }

  private def evolvePassengerCarriage(passengerCarriage : PassengerCarriage, stats : List[Double]) : Unit = {
    passengerCarriage.evolve(stats.head, stats(1), stats(2).toInt)
  }

  private def getStatsList(vehicleComponentType : VehicleComponentType, level : Int) : Option[List[Double]] = {
    val statsMap = evolutions(vehicleComponentType)

    if (statsMap.isEmpty) return None

    statsMap.get(level)
  }

  def makeEngine(engineType : EngineType, company : Company) : Engine = {
    val stats = getStatsList(engineType, 1).get

    engineType match {
      case DIESEL_ENGINE => new DieselEngine(engineType, company, stats.head, stats(1), stats(2), stats(3))
      case ELECTRIC_ENGINE => new ElectricEngine(engineType, company, stats.head, stats(1), stats(2), stats(3))
      case KEROSENE_ENGINE => new KeroseneEngine(engineType, company, stats.head, stats(1), stats(2), stats(3))
      case SHIP_ENGINE => new ShipEngine(engineType, company, stats.head, stats(1), stats(2), stats(3))
    }
  }

  def makePassengerCarriage(vehicleType : VehicleType, company : Company) : Carriage = {
    val statsOpt = vehicleType match {
      case CONCORDE => getStatsList(CONCORDE_PASSENGER_CARRIAGE, 1)

      case BOEING => getStatsList(BOEING_PASSENGER_CARRIAGE, 1)

      case _ : TrainType => getStatsList(TRAIN_PASSENGER_CARRIAGE, 1)
    }

    val stats = statsOpt.get

    new PassengerCarriage(company, stats.head, stats(1), stats(2).toInt)
  }

  def makeResourcesCarriages
  (company : Company) : ListBuffer[ResourceCarriage] = {
    ListBuffer(new ResourceCarriage(DRY_BULK, company, 5.0, 5000.0, 10000.0))

    //TODO More + update
  }

}
