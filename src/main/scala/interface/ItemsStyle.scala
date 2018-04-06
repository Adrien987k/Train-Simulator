package interface

import logic.items.ItemTypes._
import logic.world.towns.Town

import scala.collection.mutable.ListBuffer
import scalafx.scene.paint.Color

object ItemsStyle {

  class ItemStyle(val color : Color) {}

  class VehicleStyle(override val color : Color, val radius : Int)
    extends ItemStyle(color)
  {

  }

  class TownStyle(override val color : Color, val radius : Int)
    extends ItemStyle(color)
  {

  }

  class RoadStyle(override val color : Color, val width : Int, val empty : Boolean = false)
    extends ItemStyle(color)
  {

  }

  val INIT_TOWN_SIZE = 8

  val SELECTED_VEHICLE_COLOR : Color = Color.Goldenrod
  val SELECTED_ROAD_COLOR : Color = Color.Goldenrod
  val SELECTED_TOWN_COLOR : Color = Color.Black

  def ofVehicle(vehicleType : VehicleType) : VehicleStyle = {
    vehicleType match {
      case DIESEL_TRAIN => new VehicleStyle(Color.DarkRed, 5)
      case ELECTRIC_TRAIN => new VehicleStyle(Color.IndianRed, 5)
      case BOEING => new VehicleStyle(Color.DarkGray, 7)
      case CONCORDE => new VehicleStyle(Color.Grey, 9)
      case LINER => new VehicleStyle(Color.Aquamarine, 10)
      case CRUISE_BOAT => new VehicleStyle(Color.DarkCyan, 6)
      case TRUCK => new VehicleStyle(Color.Brown, 5)

      case _ => new VehicleStyle(Color.Black, 3)
    }
  }

  private def colorOfTransportFacility(transportFacilityType : TransportFacilityType) : Color = {
    transportFacilityType match {
      case STATION => Color.Red
      case AIRPORT => Color.LightGrey
      case HARBOR => Color.Blue
      case GAS_STATION => Color.Brown
    }
  }

  private def combine(a : Color, b : Color) : Color = {
    val opacity = (a.opacity + b.opacity) / 2
    val red  = (a.red + b.red) / 2
    val green = (a.green + b.green) / 2
    val blue = (a.blue + b.blue) / 2

    Color.color(red, green, blue, opacity)
  }

  private def combine(colors : List[Color]) : Color = {
    if (colors.isEmpty) return Color.Black

    colors.foldLeft(colors.head)((prevColor, color) => combine(prevColor, color))
  }

  def ofTown(town : Town) : TownStyle = {
    val colors : ListBuffer[Color] = ListBuffer.empty

    if (town.hasStation) colors += colorOfTransportFacility(STATION)
    if (town.hasAirport) colors += colorOfTransportFacility(AIRPORT)
    if (town.hasHarbor) colors += colorOfTransportFacility(HARBOR)
    if (town.hasGasStation) colors += colorOfTransportFacility(GAS_STATION)

    val size = INIT_TOWN_SIZE + colors.size * 2
    val color = combine(colors.toList)

    new TownStyle(color, size)
  }

  def ofRoad(roadType : RoadType) : RoadStyle = {
    roadType match {
      case RAIL => new RoadStyle(Color.DarkGray, 4)
      case LINE => new RoadStyle(Color.Black, 0, true)
      case WATERWAY => new RoadStyle(Color.DeepSkyBlue, 6)
      case HIGHWAY => new RoadStyle(Color.Black, 2)
    }
  }

}
