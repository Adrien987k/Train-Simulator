package logic.items.transport.vehicules

import logic.economy.{Cargo, ResourceMap}
import logic.items.EvolutionPlan
import logic.items.transport.facilities.Station
import logic.items.transport.vehicules.VehicleTypes.TrainType
import logic.items.transport.vehicules.components.{Carriage, Engine, PassengerCarriage, ResourceCarriage}
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

case class TrainEvolutionPlan
(weights : List[Double])
  extends EvolutionPlan(List(weights), 150.0, 100.0)

class Train
(val trainType : TrainType,
 override val company: Company,
 val engine : Engine,
 val carriages : ListBuffer[Carriage],
 val initialStation : Station,
 override val evolutionPlan : TrainEvolutionPlan)
  extends Vehicle(trainType, company, Some(initialStation), evolutionPlan) {

  private var weight : Double = evolutionPlan.level(level).head

  def maxSpeed() : Double = engine.maxSpeed

  override def save: xml.Node = {
    trainType.name match {
      case "Diesel Train" => <DieselTrain
        level={level.toString}
        levelengine={engine.level.toString}
        fuel={engine.fuelLevel.toString}
        >
          {carriages.foldLeft(scala.xml.NodeSeq.Empty)((acc, carriage) => acc++carriage.save)}
          {this.saveLocation}
          {this.saveGoal}
          {this.saveDestination}
          {<InitialLocation>
            <Town name={initialStation.town.name}/>
          </InitialLocation>}
        </DieselTrain>

      case "Electric Train" => <ElectricTrain
        level={level.toString}
        levelengine={engine.level.toString}
        fuel={engine.fuelLevel.toString}
        >
        {carriages.foldLeft(scala.xml.NodeSeq.Empty)((acc, carriage) => acc++carriage.save)}
        {this.saveLocation}
        {this.saveGoal}
        {this.saveDestination}
          {<InitialLocation>
            <Town name={initialStation.town.name}/>
          </InitialLocation>}
        </ElectricTrain>
    }
  }

  override def currentSpeed() : Double = {
    var speed = engine.tractiveEffort(totalWeight)

    if (currentRoad.nonEmpty && speed > currentRoad.get.speedLimit)
      speed = currentRoad.get.speedLimit

    speed
  }

  override def totalWeight : Double = {
    carriages.foldLeft(0.0)((total, carriage) =>
      total + carriage.weight
    )
  }

  override def refillFuel() : Unit = {
    engine.refillFuelLevel()
  }

  override def consume() : Unit = {
    engine.consume(totalWeight)

    if (engine.fuelLevel <= 0) crash()
  }

  override def evolve() : Unit = {
    super.evolve()

    val values = evolutionPlan.level(level)

    weight = values.head

    company.buy(evolvePrice)
  }

  override def loadPassenger(nbPassenger : Int) : Int = {
    var remainingPassenger = nbPassenger

    carriages.foreach {
      case passengerCarriage : PassengerCarriage =>
        if (remainingPassenger > 0) {
          remainingPassenger -=
            passengerCarriage.loadPassenger(remainingPassenger)
        }

      case _ =>
    }

    nbPassenger - remainingPassenger
  }

  override def unloadPassenger() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.unloadPassenger()

        case _ => total
      }
    })
  }

  override def nbPassenger() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.nbPassenger
        case _ => total
      }
    })
  }

  override def passengerCapacity() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.maxCapacity
        case _ => total
      }
    })
  }

  override def loadCargoes(cargoes : ListBuffer[Cargo]) : Unit = {
      carriages.foreach {
        case resourceCarriage : ResourceCarriage =>
          cargoes.foreach(cargo => {
            if (resourceCarriage.resourceType
                  .compareTo(cargo.resourceType) == 0) {
              resourceCarriage.loadCargo(cargo)
            }

            cargoes -= cargo
          })

        case _ =>
    }
  }

  override def unloadCargoes() : ListBuffer[Cargo] = {
    val cargoes : ListBuffer[Cargo] = ListBuffer.empty

    carriages.foldLeft(cargoes)((resources, carriage) => {
      carriage match {
        case resourceCarriage : ResourceCarriage =>
          resources ++= resourceCarriage.unloadCargos()

        case _ => resources
      }
    })
  }

  override def canTransportResource : Boolean = true

  private val trainPane = new BorderPane

  override def propertyPane() : Node = {
    trainPane.top = super.propertyPane()

    trainPane.center = engine.propertyPane()

    trainPane
  }

  private val resMap = new ResourceMap

  override def carriagesPropertyPane() : Node = {
    val resourceMap = new ResourceMap

    val map = carriages.foldLeft(resourceMap)((resourceMap, carriage) => {
      carriage match {
        case resourceCarriage : ResourceCarriage =>
          resourceMap.merge(resourceCarriage.resourceMap())

        case _ =>
          resourceMap
      }
    })

    resMap.putMap(map.resources)

    resMap.propertyPane()
  }

}
