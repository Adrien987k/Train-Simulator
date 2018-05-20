package interface

import game.Game
import logic.items.Item
import logic.Updatable
import logic.items.transport.roads.Road
import logic.items.transport.roads.RoadTypes.{RoadType, WATERWAY}
import logic.items.transport.vehicules.Vehicle
import logic.items.transport.vehicules.VehicleTypes.VehicleType
import logic.world.towns.Town
import utils.Pos

import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.control.ScrollPane
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color

object WorldCanvas extends GUIComponent {

  var canvas = new Canvas(2000, 2000)
  var gc : GraphicsContext = canvas.graphicsContext2D

  var items : ListBuffer[Item] = ListBuffer.empty

  var selectedUpdatable : Option[Updatable] = None
  var selectedVehicle : Option[Vehicle] = None

  var destinationChoice = false

  var lastPosClicked = new Pos(0,0)

  def make() : Node = {
    val scrollPane = new ScrollPane
    scrollPane.content = canvas

    scrollPane.pannable = true

    scrollPane.setHvalue(scrollPane.getHmin + (scrollPane.getHmax - scrollPane.getHmin) / 2)
    scrollPane.setVvalue(scrollPane.getVmin + (scrollPane.getVmax - scrollPane.getVmin) / 2)

    scrollPane
  }

  def restart() : Unit = {
    items = ListBuffer.empty
    selectedUpdatable = None
    selectedVehicle = None
  }

  def selectTrain(vehicle : Vehicle) : Unit = {
    selectedVehicle = Some(vehicle)
  }

  def activeDestinationChoice() : Unit = {
    destinationChoice = true
  }

  def initWorld(townsPositions : List[Pos]) : Unit = {
    canvas.onMouseClicked = (event: MouseEvent) => {
      lastPosClicked = new Pos(event.x, event.y)

      if (ItemsButtonBar.buildMode) {
        ItemsButtonBar.selectedItem match {
          case Some(item) => Game.world.company.tryPlace(item, lastPosClicked)
          case _ =>
        }
      } else {
        if (destinationChoice) {
          Game.world.company.setVehicleDestination(lastPosClicked)
          destinationChoice = false
        }
        else LocalInformationPanel.selectUpdatableAt(lastPosClicked)
      }
    }

    canvas.onMouseMoved = (event: MouseEvent) => {
      val pos = new Pos(event.x, event.y)

      Game.world.updatableAt(pos) match {
        case Some(updatable) =>
          selectedUpdatable = Some(updatable)

        case None =>
          selectedUpdatable = None
      }
    }
  }

  def displaySelection() : Unit = {
    if (selectedUpdatable.isEmpty) return

    gc.stroke = ItemsStyle.SELECTED_TOWN_COLOR
    gc.lineWidth = 3

    selectedUpdatable.get match {
      case town : Town =>
        val style = ItemsStyle.ofTown(town)
        if (destinationChoice) gc.stroke = ItemsStyle.SELECTED_VEHICLE_COLOR

        gc.strokeRect(town.pos.x - style.radius * 1.25, town.pos.y - style.radius * 1.25, style.radius * 2.5, style.radius * 2.5)

      case item : Item =>
        item.itemType match {
          case t : VehicleType =>
            val style = ItemsStyle.ofVehicle(t)

            val vehicle = item.asInstanceOf[Vehicle]

            gc.stroke = ItemsStyle.SELECTED_VEHICLE_COLOR
            gc.strokeRect(vehicle.pos.x - style.radius * 2,
              vehicle.pos.y - style.radius * 2,
              style.radius * 4,
              style.radius * 4)

          case t : RoadType =>
            val style = ItemsStyle.ofRoad(t)

            if (!style.empty) {
              gc.lineWidth = style.width * 2
              gc.stroke = ItemsStyle.SELECTED_ROAD_COLOR

              val road = item.asInstanceOf[Road]

              gc.strokeLine(road.posA.x, road.posA.y, road.posB.x, road.posB.y)
            }
        }
      case _ =>
    }
  }

  def update() : Unit = {
    gc.fill = Color.White
    gc.fillRect(0, 0, canvas.width(), canvas.height())
    gc.stroke = Color.Black
    gc.lineWidth = 3

    Game.world.towns.foreach(town => {
      val style = ItemsStyle.ofTown(town)
      gc.fill = style.color
      gc.fillOval(town.pos.x - style.radius, town.pos.y - style.radius, style.radius * 2, style.radius * 2)
    })

    Game.world.company.roads.foreach(road => {
      val style = ItemsStyle.ofRoad(road.roadType)
      gc.stroke = style.color
      gc.lineWidth = style.width

      if (!style.empty)
        gc.strokeLine(road.posA.x, road.posA.y, road.posB.x, road.posB.y)
    })

    Game.world.naturalWaterways.foreach(waterway => {
      val style = ItemsStyle.ofRoad(WATERWAY)
      gc.stroke = style.color
      gc.lineWidth = style.width

      if (!style.empty)
        gc.strokeLine(waterway.townA.pos.x, waterway.townA.pos.y, waterway.townB.pos.x, waterway.townB.pos.y)
    })

    if (selectedVehicle.nonEmpty) {
      val style = ItemsStyle.ofVehicle(selectedVehicle.get.vehicleType)

      val pos = selectedVehicle.get.pos

      gc.lineWidth = 3
      gc.stroke = ItemsStyle.SELECTED_VEHICLE_COLOR

      gc.strokeRect(pos.x - style.radius * 1.25,
        pos.y - style.radius * 1.25, style.radius * 2.5, style.radius * 2.5)
    }

    displayVehicle()
    displaySelection()
  }

  def displayVehicle() : Unit = {
    Game.world.company.vehicles.foreach(vehicle => {
      val style = ItemsStyle.ofVehicle(vehicle.vehicleType)

      gc.fill = style.color
      gc.fillRect(vehicle.pos.x - style.radius, vehicle.pos.y - style.radius,
          style.radius * 2, style.radius * 2)
      }
    )
  }

  def removeIfSelected(vehicle : Vehicle) : Unit = {
    if (selectedVehicle.nonEmpty && selectedVehicle.get == vehicle)
      selectedVehicle = None
  }

}
