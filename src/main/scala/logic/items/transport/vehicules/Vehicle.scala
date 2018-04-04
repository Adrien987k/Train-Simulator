package logic.items.transport.vehicules

import game.Game
import interface.{ItemsButtonBar, WorldCanvas}
import logic.exceptions.ImpossibleActionException
import logic.{PointUpdatable, UpdateRate}
import logic.items.Item
import logic.items.ItemTypes.VehicleType
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.roads.Road
import logic.items.transport.vehicules.components.{Carriage, Engine, PassengerCarriage}
import logic.world.Company
import logic.world.towns.Town
import utils.{Dir, Pos}

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.scene.text.{Font, FontWeight}

abstract class Vehicle
(val vehicleType : VehicleType,
 override val company : Company,
 val engine : Engine,
 val carriages : ListBuffer[Carriage],
 var currentTransportFacility : Option[TransportFacility])
  extends Item(vehicleType, company) with PointUpdatable {

  updateRate(UpdateRate.VEHICLE_UPDATE)

  pos = currentTransportFacility match {
    case Some(tf) => tf.pos.copy()
    case None => new Pos(0, 0)
  }

  var dir : Dir = new Dir(0, 0)

  /* The next facility to reach */
  var goalTransportFacility : Option[TransportFacility] = None

  /* The final town to reach */
  var destination : Option[TransportFacility] = None

  var currentRoad : Option[Road] = None

  private var _crashed = false

  def crashed : Boolean = _crashed

  override def step() : Boolean = {
    if(!super.step()) return false

    if (crashed) return false

    if (currentRoad.isEmpty) return false

    goalTransportFacility match {
      case Some(transportFacility) =>
        if (pos.inRange(transportFacility.pos, 10)) {

          removeFromRoad()

          println("step goal TF : " + transportFacility.town.name)
          transportFacility.unload(this)

        } else {
          val speed = currentSpeed()
          pos.x += dir.x * speed
          pos.y += dir.y * speed

          consume()
        }
      case None =>
    }

    true
  }

  def currentSpeed() : Double = {
    var speed = engine.tractiveEffort(totalWeight)

    if (currentRoad.nonEmpty && speed > currentRoad.get.speedLimit)
      speed = currentRoad.get.speedLimit

    speed
  }

  def totalWeight : Double = {
    carriages.foldLeft(0.0)((total, carriage) =>
      total + carriage.weight
    )
  }

  def refillFuel() : Unit = {
    engine.refillFuelLevel()
  }

  def consume() : Unit = {
    engine.consume(totalWeight)

    if (engine.fuelLevel <= 0) crash()
  }

  def crash() : Unit = {
    //TODO GUI.crash(pos)

    _crashed = true
  }

  def setObjective(transportFacility : TransportFacility) : Unit = {
    if (goalTransportFacility.nonEmpty) return
    println("YEAD")

    goalTransportFacility = Some(transportFacility)

    dir.x = transportFacility.pos.x - pos.x
    dir.y = transportFacility.pos.y - pos.y
    dir.normalize()
  }

  def unsetObjective() : Unit = {
    goalTransportFacility = None
  }

  def setDestination(town : Town) : Unit = {
    destination = town.transportFacilityForVehicleType(vehicleType)
  }

  def putOnRoad(road : Road) : Unit = {
    if (road.isFull) throw new ImpossibleActionException("Road is full")
    currentRoad match {
      case Some(_) =>
        throw new ImpossibleActionException("Train already in a road")
      case None =>
        currentTransportFacility = None
        road.addVehicle(this)

        println("PUT ON ROAD SUCCESS")
        currentRoad = Some(road)
    }
  }

  def removeFromRoad() : Unit = {
    currentRoad match {
      case Some(road) =>
        road.removeVehicle(this)
        currentRoad = None
      case None =>
    }
  }

  def addCarriage(carriage : Carriage) : Unit = {
    carriages += carriage
  }

  override def evolve() : Unit = {
    engine.evolve()
  }

  /**
    * @param nbPassenger The number of passenger to load
    * @return The number of loaded passenger
    */
  def loadPassenger(nbPassenger : Int) : Int = {
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

  /**
    * Empty the train of its passengers
    *
    * @return The number of passenger in the train
    */
  def unloadPassenger() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.unloadPassenger()
        case _ => total
      }
    })
  }

  /**
    * @return The total number of passenger in the train
    */
  def nbPassenger() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.nbPassenger
        case _ => total
      }
    })
  }

  /**
    * @return The total passenger capacity of this train
    */
  def passengerCapacity() : Int = {
    carriages.foldLeft(0)((total, carriage) => {
      carriage match {
        case passengerCarriage : PassengerCarriage =>
          total + passengerCarriage.maxCapacity
        case _ => total
      }
    })
  }

  val pane = new BorderPane

  val panel = new VBox()

  val typeLabel = new Label(vehicleType.name)
  val speedLabel = new Label()
  val maxPassengerLabel = new Label()
  val nbPassengerLabel = new Label()
  val posLabel = new Label()
  val goalStationLabel = new Label()
  val destinationLabel = new Label()

  labels = List(typeLabel,
    speedLabel,
    maxPassengerLabel,
    nbPassengerLabel,
    posLabel,
    goalStationLabel,
    destinationLabel)

  panel.children = labels

  styleLabels()

  pane.top = panel

  val chooseDestPanel = new Button("Choose destination")

  override def propertyPane() : Node = {
    typeLabel.text = vehicleType.name
    speedLabel.text = "Max Speed : " + engine.maxSpeed
    maxPassengerLabel.text = "Max passengers : " + passengerCapacity
    nbPassengerLabel.text = "Passengers : " + nbPassenger
    posLabel.text = "Position : " + pos

    if (goalTransportFacility.nonEmpty) {
      goalStationLabel.text = "Next goal : " + goalTransportFacility.get.town.name

      if (!panel.children.contains(goalStationLabel))
        panel.children.add(goalStationLabel)
    }

    if (destination.nonEmpty) {
      destinationLabel.text = "Destination : " + destination.get.town.name

      if (!panel.children.contains(destinationLabel))
        panel.children.add(destinationLabel)
    } else {
      destinationLabel.text = ""
    }

    chooseDestPanel.font = Font.font(null, FontWeight.Bold, 18)

    chooseDestPanel.onAction = _ => {
      if (!ItemsButtonBar.buildMode) {
        Game.world.company.selectVehicle(this)
        WorldCanvas.activeDestinationChoice()
      }
    }

    if (!panel.children.contains(chooseDestPanel))
      panel.children.add(chooseDestPanel)

    pane.center = engine.propertyPane()

    pane
  }

}
