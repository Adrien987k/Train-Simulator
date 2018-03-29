package interface

import logic.items.ItemTypes._

import scalafx.scene.paint.Color

class VehicleStyle
(val color : Color,
 val radius : Int)
{

}

object ItemsStyle {

  def ofVehicle(vehicleType : VehicleType) : VehicleStyle = {
    vehicleType match {
      case DIESEL_TRAIN => new VehicleStyle(Color.BlueViolet, 5)
      case ELECTRIC_TRAIN => new VehicleStyle(Color.DarkGreen, 5)
      case BOEING => new VehicleStyle(Color.DarkGray, 7)
      case CONCORDE => new VehicleStyle(Color.DarkCyan, 9)
    }
  }

}
