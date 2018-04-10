package logic.items.production

import logic.items.ItemTypes.FacilityType

object FactoryTypes {

  abstract class FactoryType(name : String) extends FacilityType(name)

  abstract class FarmsType(name : String) extends FactoryType(name)
  abstract class RanchesType(name : String) extends FactoryType(name)
  abstract class MinesType(name : String) extends FactoryType(name)
  abstract class SimpleFactoryType(name : String) extends FactoryType(name)

  case object ALUMINIUM_PLANT extends SimpleFactoryType("Aluminium plant")

  case object GRAIN_FARM extends FarmsType("Grain farm")
  case object GARDEN extends FarmsType("Garden")

  case object IRON_MINE extends MinesType("Iron")
  case object BAUXITE_MINE extends MinesType("Bauxite mine")
  case object COAL_MINE extends MinesType("Coal mine")

  case object TEXTILE_FACTORY extends SimpleFactoryType("Textile factory")

  def factories() : List[FactoryType] = {
    List(GRAIN_FARM, GARDEN, IRON_MINE, BAUXITE_MINE, COAL_MINE, TEXTILE_FACTORY)
  }

}
