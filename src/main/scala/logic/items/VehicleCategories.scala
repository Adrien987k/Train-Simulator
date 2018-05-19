package logic.items

object VehicleCategories {

  sealed class VehicleCategory

  case object Trains extends VehicleCategory
  case object Planes extends VehicleCategory
  case object Ships extends VehicleCategory
  case object Trucks extends VehicleCategory

}
