package logic.items.transport.vehicules

import logic.economy.ResourcesTypes
import logic.exceptions.AlreadyMaxLevelException
import logic.items.transport.vehicules.VehicleComponentTypes._

object VehicleComponentFactory {

  val dieselEngineEvolutions = Map(
    1 -> List(5.0, 3000.0, 100000.0, 50000.0),
    2 -> List(6.0, 4000.0, 100000.0, 60000.0),
    3 -> List(20.0, 5000.0, 120000.0, 70000.0)
  )

  val electricEngineEvolutions = Map(
    1 -> List(3.0, 10000.0, 50000.0, 1500.0),
    2 -> List(4.0, 10000.0, 50000.0, 2000.0),
    3 -> List(5.0, 12000.0, 60000.0, 2500.0)
  )

  val keroseneEngineEvolutions = Map(
    1 -> List(15.0, 30000.0, 100000.0, 2000000.0),
    2 -> List(20.0, 50000.0, 150000.0, 3000000.0),
    3 -> List(25.0, 75000.0, 200000.0, 3000000.0)
  )

  def evolve(vehicleComponent : VehicleComponent) : Unit = {
    vehicleComponent.vehicleComponentType match {
      case _ : EngineType => evolveEngine(vehicleComponent.asInstanceOf[Engine])

      //TODO other types
      case _ =>
    }
  }

  private def evolveEngine(engine : Engine) : Unit = {
    val statsOpt = getStatsList(engine.engineType, engine.level + 1)

    if (statsOpt.isEmpty) throw new AlreadyMaxLevelException("This vehicle is max level")

    val stats = statsOpt.get

    println("EVOLUTION")
    engine.level += 1
    engine.evolve(stats.head, stats(1), stats(2), stats(3))
  }

  private def getStatsList(vehicleComponentType : VehicleComponentType, level : Int) : Option[List[Double]] = {
    val statsMap = vehicleComponentType match {
      case DIESEL_ENGINE => dieselEngineEvolutions
      case ELECTRIC_ENGINE => electricEngineEvolutions
      case KEROSENE_ENGINE => keroseneEngineEvolutions

      //case _ =>
    }

    statsMap.get(level)
  }

  def makeEngine(engineType : VehicleComponentTypes.EngineType) : Engine = {
    val stats = getStatsList(engineType, 1).get

    engineType match {
      case DIESEL_ENGINE => new DieselEngine(engineType, stats.head, stats(1), stats(2), stats(3))
      case ELECTRIC_ENGINE => new ElectricEngine(engineType, stats.head, stats(1), stats(2), stats(3))
      case KEROSENE_ENGINE => new KeroseneEngine(engineType, stats.head, stats(1), stats(2), stats(3))
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
