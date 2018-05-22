package logic.items.production

import logic.items.ItemTypes.FacilityType

object FactoryTypes {

  abstract class FactoryType(name : String, price : Double) extends FacilityType(name, price)

  abstract class FarmsType(name : String, price : Double) extends FactoryType(name, price)
  abstract class RanchesType(name : String, price : Double) extends FactoryType(name, price)
  abstract class MinesType(name : String, price : Double) extends FactoryType(name, price)
  abstract class SimpleFactoryType(name : String, price : Double) extends FactoryType(name, price)

  case object ALUMINIUM_PLANT extends SimpleFactoryType("Aluminium plant", 1000)
  case object BAKERY extends SimpleFactoryType("Bakery", 1000)
  case object BAUXITE_MINE extends MinesType("Bauxite mine", 1000)
  case object BREWERY extends SimpleFactoryType("Brewery", 1000)
  case object BRICKWORKS extends SimpleFactoryType("Brickworks", 1000)
  case object CANNERY extends SimpleFactoryType("Cannery", 1000)
  case object CATTLE_RANCH extends RanchesType("Cattle ranch", 1000)
  case object CEMENT_FACTORY extends SimpleFactoryType("Cement factory", 1000)
  case object CHEMICAL_PLANT extends SimpleFactoryType("Chemical plant", 1000)
  case object CLAY_PIT extends MinesType("Clay pit", 1000)
  case object COAL_MINE extends MinesType("Coal mine", 1000)
  case object COPPER_MINE extends MinesType("Copper mine", 1000)
  case object COTTON_PLANTATION extends FarmsType("Cotton plantation", 1000)
  case object ELECTRONICS_FACTORY extends SimpleFactoryType("Electronics factory", 1000)
  case object FISHERY extends RanchesType("Fishery", 1000)
  case object FORESTRY extends FarmsType("Forestry", 1000)
  case object FRUIT_ORCHARD extends FarmsType("Fruit orchard", 1000)
  case object FURNITURE_FACTORY extends SimpleFactoryType("Furniture factory", 1000)
  case object GLASS_FACTORY extends SimpleFactoryType("Glass factory", 1000)
  case object GRAIN_FARM extends FarmsType("Grain farm", 1000)
  case object IRON_MINE extends MinesType("Iron mine", 1000)
  case object LIQUOR_DISTILLERY extends SimpleFactoryType("Liquor distillery", 1000)
  case object LUMBER_MILL extends SimpleFactoryType("Lumber mill", 1000)
  case object OIL_REFINERY extends SimpleFactoryType("Oil refinery", 1000)
  case object OIL_WELLS extends MinesType("Oil wells", 1000)
  case object PAPER_MILL extends SimpleFactoryType("Paper mill", 1000)
  case object PIG_FARM extends RanchesType("Pig farm", 1000)
  case object PRINTING_PRESS extends SimpleFactoryType("Printing press", 1000)
  case object QUARRY extends MinesType("Quarry", 1000)
  case object RUBBER_PLANTATION extends FarmsType("Rubber plantation", 1000)
  case object SAND_PIT extends MinesType("Sand pit", 1000)
  case object SHEEP_FARM extends RanchesType("Sheep farm", 1000)
  case object STEEL_MILL extends SimpleFactoryType("Steel mill", 1000)
  case object STOCKYARD extends SimpleFactoryType("Stockyard", 1000)
  case object TANNERY extends SimpleFactoryType("Tannery", 1000)
  case object TEXTILE_MILL extends SimpleFactoryType("Textile mill", 1000)
  case object TYRE_FACTORY extends SimpleFactoryType("Tyre factory", 1000)
  case object VEGETABLE_FARM extends FarmsType("Vegetable farm", 1000)
  case object VEHICLE_FACTORY extends SimpleFactoryType("Vehicle factory", 1000)
  case object VINEYARD extends FarmsType("Vineyard", 1000)
  case object WIRE_MILL extends SimpleFactoryType("Wire mill", 1000)

  def factoriesTest() : List[FactoryType] = {
    List(GRAIN_FARM, COAL_MINE, BAUXITE_MINE, ALUMINIUM_PLANT, BAKERY, SHEEP_FARM)
  }

  def factories() : List[FactoryType] = {
    List(ALUMINIUM_PLANT,
    BAKERY,
    BAUXITE_MINE,
    //BREWERY,
    //BRICKWORKS,
    //CANNERY,
    //CATTLE_RANCH,
    //CEMENT_FACTORY,
    //CHEMICAL_PLANT,
    //CLAY_PIT,
    COAL_MINE,
    COPPER_MINE,
    COTTON_PLANTATION,
    //ELECTRONICS_FACTORY,
    FISHERY,
    //FORESTRY,
    //FRUIT_ORCHARD,
    FURNITURE_FACTORY,
    //GLASS_FACTORY,
    GRAIN_FARM,
    IRON_MINE,
    //LIQUOR_DISTILLERY,
    //LUMBER_MILL,
    OIL_REFINERY,
    OIL_WELLS,
    //PAPER_MILL,
    //PIG_FARM,
    //PRINTING_PRESS,
    //QUARRY,
    //RUBBER_PLANTATION,
    //SAND_PIT,
    SHEEP_FARM,
    STEEL_MILL,
    //STOCKYARD,
    TANNERY,
    TEXTILE_MILL
    //TYRE_FACTORY,
    //VEHICLE_FACTORY,
    //VEGETABLE_FARM,
    //VINEYARD,
    //WIRE_MILL)
    )
  }

}
