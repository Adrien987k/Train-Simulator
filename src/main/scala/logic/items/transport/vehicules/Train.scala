package logic.items.transport.vehicules

import logic.economy.{Cargo, ResourceMap}
import logic.items.EvolutionPlan
import logic.items.transport.facilities.Station
import logic.items.transport.vehicules.VehicleTypes.TrainType
import logic.items.transport.vehicules.components.{Carriage, Engine, PassengerCarriage, ResourceCarriage}
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node

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

    engine.evolve()

    val values = evolutionPlan.level(level)

    weight = values.head
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

  override def carriagesPropertyPane() : Node = {
    val resourceMap = new ResourceMap

    carriages.foldLeft(resourceMap)((resourceMap, carriage) => {
      carriage match {
        case resourceCarriage : ResourceCarriage =>
          resourceMap.merge(resourceCarriage.resourceMap())

        case _ =>
          resourceMap
      }
    }).propertyPane()
  }

  override def canTransportResource : Boolean = true

}
