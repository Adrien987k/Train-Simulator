package logic.items.production

import logic.items.ItemTypes.FacilityType

object FactoryTypes {

  abstract class FactoryType(name : String) extends FacilityType(name)

  abstract class FarmsType(name : String) extends FactoryType(name)
  abstract class RanchesType(name : String) extends FactoryType(name)
  abstract class MinesType(name : String) extends FactoryType(name)
  abstract class SimpleFactoryType(name : String) extends FactoryType(name)

  case object ALUMINIUM_PLANT extends SimpleFactoryType("Aluminium plant")
  case object BAKERY extends SimpleFactoryType("Bakery")
  case object BAUXITE_MINE extends MinesType("Bauxite mine")
  case object BREWERY extends SimpleFactoryType("Brewery")
  case object BRICKWORKS extends SimpleFactoryType("Brickworks")
  case object CANNERY extends SimpleFactoryType("Cannery")
  case object CATTLE_RANCH extends RanchesType("Cattle ranch")
  case object CEMENT_FACTORY extends SimpleFactoryType("Cement factory")
  case object CHEMICAL_PLANT extends SimpleFactoryType("Chemical plant")
  case object CLAY_PIT extends MinesType("Clay pit")
  case object COAL_MINE extends MinesType("Coal mine")
  case object COPPER_MINE extends MinesType("Copper mine")
  case object COTTON_PLANTATION extends FarmsType("Cotton plantation")
  case object ELECTRONICS_FACTORY extends SimpleFactoryType("Electronics factory")
  case object FISHERY extends RanchesType("Fishery")
  case object FORESTRY extends FarmsType("Forestry")
  case object FRUIT_ORCHARD extends FarmsType("Fruit orchard")
  case object FURNITURE_FACTORY extends SimpleFactoryType("Furniture factory")
  case object GLASS_FACTORY extends SimpleFactoryType("Glass factory")
  case object GRAIN_FARM extends FarmsType("Grain farm")
  case object IRON_MINE extends MinesType("Iron mine")
  case object LIQUOR_DISTILLERY extends SimpleFactoryType("Liquor distillery")
  case object LUMBER_MILL extends SimpleFactoryType("Lumber mill")
  case object OIL_REFINERY extends SimpleFactoryType("Oil refinery")
  case object OIL_WELLS extends MinesType("Oil wells")
  case object PAPER_MILL extends SimpleFactoryType("Paper mill")
  case object PIG_FARM extends RanchesType("Pig farm")
  case object PRINTING_PRESS extends SimpleFactoryType("Printing press")
  case object QUARRY extends MinesType("Quarry")
  case object RUBBER_PLANTATION extends FarmsType("Rubber plantation")
  case object SAND_PIT extends MinesType("Sand pit")
  case object SHEEP_FARM extends RanchesType("Sheep farm")
  case object STEEL_MILL extends SimpleFactoryType("Steel mill")
  case object STOCKYARD extends SimpleFactoryType("Stockyard")
  case object TANNERY extends SimpleFactoryType("Tannery")
  case object TEXTILE_MILL extends SimpleFactoryType("Textile mill")
  case object TYRE_FACTORY extends SimpleFactoryType("Tyre factory")
  case object VEGETABLE_FARM extends FarmsType("Vegetable farm")
  case object VEHICLE_FACTORY extends SimpleFactoryType("Vehicle factory")
  case object VINEYARD extends FarmsType("Vineyard")
  case object WIRE_MILL extends SimpleFactoryType("Wire mill")

  def factories() : List[FactoryType] = {
    List(GRAIN_FARM, COAL_MINE, BAUXITE_MINE, ALUMINIUM_PLANT, BAKERY, SHEEP_FARM, TEXTILE_MILL)
  }

}
