package interface

import logic.items.ItemTypes._
import logic.world.towns.Town

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

  //TODO Complete !!!
  
  def ofVehicle(vehicleType : VehicleType) : VehicleStyle = {
    vehicleType match {
      case DIESEL_TRAIN => new VehicleStyle(Color.BlueViolet, 5)
      case ELECTRIC_TRAIN => new VehicleStyle(Color.DarkGreen, 5)
      case BOEING => new VehicleStyle(Color.DarkGray, 7)
      case CONCORDE => new VehicleStyle(Color.DarkCyan, 9)

    }
  }

  def ofTown(town : Town) : TownStyle = {
    if (town.hasStation && town.hasAirport)
      return new TownStyle(Color.DarkBlue, 15)

    if (town.hasStation) return new TownStyle(Color.Green, 10)

    if (town.hasAirport) return new TownStyle(Color.Aquamarine, 10)

    new TownStyle(Color.Red, 8)
  }

  def ofRoad(roadType : RoadType) : RoadStyle = {
    roadType match {
      case RAIL => new RoadStyle(Color.Black, 3)
      case LINE => new RoadStyle(Color.Black, 0, true)
    }
  }

}
