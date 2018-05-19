package logic.items.transport.vehicules

import game.Game
import interface.{ItemsButtonBar, WorldCanvas}
import logic.economy.Cargo
import logic.exceptions.ImpossibleActionException
import logic.{PointUpdatable, UpdateRate}
import logic.items.{EvolutionPlan, Item}
import logic.items.transport.facilities.TransportFacility
import logic.items.transport.roads.Road
import logic.items.transport.vehicules.VehicleTypes.VehicleType
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
 var currentTransportFacility : Option[TransportFacility],
 override val evolutionPlan : EvolutionPlan)
  extends Item(vehicleType, company, evolutionPlan) with PointUpdatable {

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

          leaveRoad()

          transportFacility.unload(this)

        } else {
          var speed = currentSpeed()
          if (currentRoad.nonEmpty && speed > currentRoad.get.speedLimit)
            speed = currentRoad.get.speedLimit

          pos.x += dir.x * speed
          pos.y += dir.y * speed

          consume()
        }
      case None =>
    }

    true
  }

  /**
    * @return The maximum speed this vehicle can reach
    */
  def maxSpeed() : Double

  /**
    * @return The current speed of this vehicle
    */
  def currentSpeed() : Double

  /**
    * @return The total weight of this vehicle
    */
  def totalWeight : Double

  /**
    * reFill the fuel level of this vehicle
    */
  def refillFuel()

  /**
    * Consume some fuel
    */
  def consume() : Unit

  /**
    * Crash this vehicle
    * It disappears of the map
    */
  def crash() : Unit = {
    //TODO GUI.crash(pos)

    _crashed = true
  }

  /**
    * @return True if this vehicle is able to
    *         transport resources
    */
  def canTransportResource : Boolean

  /**
    * Set an objective to this vehicle
    * The objective is the next transport facility to reach
    *
    * @param transportFacility The objective
    */
  def setObjective(transportFacility : TransportFacility) : Unit = {
    if (goalTransportFacility.nonEmpty) return

    goalTransportFacility = Some(transportFacility)

    dir.x = transportFacility.pos.x - pos.x
    dir.y = transportFacility.pos.y - pos.y
    dir.normalize()
  }

  /**
    * Remove the current objective
    */
  def unsetObjective() : Unit = {
    goalTransportFacility = None
  }

  /**
    * Set a destination to this vehicle
    * A destination is the final town to reach
    *
    * @param town The destination
    */
  def setDestination(town : Town) : Unit = {
    destination = town.transportFacilityForVehicleType(vehicleType)
  }

  /**
    * Move this vehicle from its facility to the road [Road]
    *
    * @param road The road to enter
    */
  def enterRoad(road : Road) : Unit = {
    if (road.isFull) throw new ImpossibleActionException("Road is full")
    currentRoad match {
      case Some(_) =>
        throw new ImpossibleActionException("Train already in a road")
      case None =>
        currentTransportFacility = None
        road.addVehicle(this)

        currentRoad = Some(road)
    }
  }

  /**
    * make this vehicle leave its current road
    */
  def leaveRoad() : Unit = {
    currentRoad match {
      case Some(road) =>
        road.removeVehicle(this)
        currentRoad = None
      case None =>
    }
  }

  /**
    * Evolve this vehicle to the superior level
    * It improve its characteristics
    */
  override def evolve() : Unit = super.evolve()

  /**
    * @param nbPassenger The number of passenger to load
    * @return The number of loaded passenger
    */
  def loadPassenger(nbPassenger : Int) : Int

  /**
    * Empty the train of its passengers
    *
    * @return The number of passenger in the train
    */
  def unloadPassenger() : Int

  /**
    * @return The total number of passenger in the train
    */
  def nbPassenger() : Int

  /**
    * @return The total passenger capacity of this train
    */
  def passengerCapacity() : Int

  /**
    * Load the resources packs in this vehicle
    *
    * @param cargoes The cargos to load
    */
  def loadCargoes(cargoes : ListBuffer[Cargo])

  /**
    * Empty the train of its resources
    *
    * @return The resources
    */
  def unloadCargoes() : ListBuffer[Cargo]

  /**
    * @return A String with all the information to display
    *         about this vehicle
    */
  def carriagesPropertyPane() : Node

  val pane = new BorderPane

  val panel = new VBox

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

  var carriageInfo : Node = new Label("")
  panel.children.add(carriageInfo)

  override def propertyPane() : Node = {
    typeLabel.text = "=== " + vehicleType.name + " ==="
    speedLabel.text = "Max Speed : " + maxSpeed()
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

    carriageInfo = carriagesPropertyPane()

    pane
  }

}
