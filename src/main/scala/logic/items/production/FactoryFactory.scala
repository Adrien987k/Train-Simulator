package logic.items.production

import logic.economy.Resources._
import logic.items.production.FactoryTypes._
import logic.world.Company
import logic.world.towns.Town
import utils.DateTime

import scala.collection.mutable.ListBuffer

object FactoryFactory {

  def make(factoryType : FactoryType, company : Company, town : Town) : Factory = {

    factoryType match {

      case ALUMINIUM_PLANT =>
        new Factory(ALUMINIUM_PLANT, company, town,
          ListBuffer(
            Recipe(List((BAUXITE, 1000), (COAL, 500)), (ALUMINIUM, 500), new DateTime(15, 0))
          )
        )

      case BAUXITE_MINE =>
        new Factory(BAUXITE_MINE, company, town,
          ListBuffer(
            Recipe(List(), (BAUXITE, 1000), new DateTime(10, 0))
          )
        )

      case BAKERY =>
        new Factory(BAKERY, company, town,
          ListBuffer(
            Recipe(List((GRAIN, 500)), (BAKED_GOOD, 100), new DateTime(5, 0))
          )
        )

      case BREWERY =>
        new Factory(BREWERY, company, town,
          ListBuffer(
            Recipe(List((GRAIN, 1000), (GLASS, 100)), (BEER, 200), new DateTime(20, 0))
          )
        )

      case BRICKWORKS =>
        new Factory(BRICKWORKS, company, town,
          ListBuffer(
            Recipe(List((CLAY, 1000)), (BRICK, 500), new DateTime(15, 0))
          )
        )

      case CANNERY =>
        new Factory(CANNERY, company, town,
          ListBuffer(
            Recipe(List((STEEL, 50), (FISH, 200)), (CANNED_FOOD, 100), new DateTime(12, 0)),
            Recipe(List((STEEL, 50), (FRUIT, 200)), (CANNED_FOOD, 100), new DateTime(12, 0)),
            Recipe(List((STEEL, 50), (MEAT, 200)), (CANNED_FOOD, 100), new DateTime(12, 0)),
            Recipe(List((STEEL, 50), (VEGETABLE, 200)), (CANNED_FOOD, 100), new DateTime(12, 0))
          )
        )

      case CATTLE_RANCH =>
        new Factory(CATTLE_RANCH, company, town,
          ListBuffer(
            Recipe(List((GRAIN, 2500)), (CATTLE, 100), new DateTime(20, 0))
          )
        )

      case CEMENT_FACTORY =>
        new Factory(CEMENT_FACTORY, company, town,
          ListBuffer(
            Recipe(List((COAL, 500), (LIMESTONE, 1000)), (CEMENT, 200), new DateTime(15, 0))
          )
        )

      case CHEMICAL_PLANT =>
        new Factory(CHEMICAL_PLANT, company, town,
          ListBuffer(
            Recipe(List((PETROLEUM_PRODUCT, 1000)), (CHEMICAL, 200), new DateTime(15, 0)),
            Recipe(List((PETROLEUM_PRODUCT, 1000)), (PLASTIC, 500), new DateTime(15, 0))
          )
        )

      case CLAY_PIT =>
        new Factory(CLAY_PIT, company, town,
          ListBuffer(
            Recipe(List(), (CLAY, 1000), new DateTime(10, 0))
          )
        )

      case COAL_MINE =>
        new Factory(COAL_MINE, company, town,
          ListBuffer(
            Recipe(List(), (COAL, 1000), new DateTime(10, 0))
          )
        )

      case COPPER_MINE =>
        new Factory(COPPER_MINE, company, town,
          ListBuffer(
            Recipe(List(), (COPPER, 1000), new DateTime(10, 0))
          )
        )

      case COTTON_PLANTATION =>
        new Factory(COTTON_PLANTATION, company, town,
          ListBuffer(
            Recipe(List(), (COTTON, 5000), new DateTime(30, 0))
          )
        )

      case ELECTRONICS_FACTORY =>
        new Factory(ELECTRONICS_FACTORY, company, town,
          ListBuffer(
            Recipe(List((GLASS, 500), (PLASTIC, 500),(ALUMINIUM_WIRE, 100)), (ELECTRONICS, 1500), new DateTime(12, 0)),
            Recipe(List((GLASS, 500), (PLASTIC, 500), (COPPER_WIRE, 100)), (ELECTRONICS, 1500), new DateTime(12, 0))
          )
        )

      case FISHERY =>
        new Factory(FISHERY, company, town,
          ListBuffer(
            Recipe(List(), (FISH, 1000), new DateTime(10, 0))
          )
        )

      case FORESTRY =>
        new Factory(FORESTRY, company, town,
          ListBuffer(
            Recipe(List(), (TIMBER, 100), new DateTime(5, 0))
          )
        )

      case FRUIT_ORCHARD =>
        new Factory(FRUIT_ORCHARD, company, town,
          ListBuffer(
            Recipe(List(), (COAL, 500), new DateTime(30, 0))
          )
        )

      case FURNITURE_FACTORY =>
        new Factory(FURNITURE_FACTORY, company, town,
          ListBuffer(
            Recipe(List((LUMBER, 1000), (GLASS, 200)), (FURNITURE, 100), new DateTime(40, 0)),
            Recipe(List((LUMBER, 1000), (LEATHER, 500)), (FURNITURE, 100), new DateTime(40, 0)),
            Recipe(List((LUMBER, 1000), (TEXTILE, 1000)), (FURNITURE, 100), new DateTime(40, 0)),
            Recipe(List((STEEL, 500), (GLASS, 200)), (FURNITURE, 100), new DateTime(20, 0)),
            Recipe(List((STEEL, 500), (LEATHER, 500)), (FURNITURE, 100), new DateTime(20, 0)),
            Recipe(List((STEEL, 500), (TEXTILE, 1000)), (FURNITURE, 100), new DateTime(20, 0))
          )
        )

      case GLASS_FACTORY =>
        new Factory(GLASS_FACTORY, company, town,
          ListBuffer(
            Recipe(List((COAL, 500), (SAND, 1000)), (GLASS, 1000), new DateTime(15, 0))
          )
        )

      case GRAIN_FARM =>
        new Factory(GRAIN_FARM, company, town,
          ListBuffer(
            Recipe(List(), (GRAIN, 10000), new DateTime(30, 0))
          )
        )

      case IRON_MINE =>
        new Factory(IRON_MINE, company, town,
          ListBuffer(
            Recipe(List(), (IRON, 1000), new DateTime(10, 0))
          )
        )

      case LIQUOR_DISTILLERY =>
        new Factory(LIQUOR_DISTILLERY, company, town,
          ListBuffer(
            Recipe(List((GLASS, 100), (FRUIT, 500)), (LIQUOR, 200), new DateTime(25, 0)),
            Recipe(List((GLASS, 100), (GRAIN, 1000)), (LIQUOR, 200), new DateTime(25, 0))
          )
        )

      case LUMBER_MILL =>
        new Factory(LUMBER_MILL, company, town,
          ListBuffer(
            Recipe(List((TIMBER, 100)), (LUMBER, 500), new DateTime(12, 0)),
            Recipe(List((TIMBER, 10)), (WOODCHIP, 200), new DateTime(12, 0))
          )
        )

      case OIL_REFINERY =>
        new Factory(OIL_REFINERY, company, town,
          ListBuffer(
            Recipe(List((OIL, 1500)), (FUEL, 1000), new DateTime(12, 0)),
            Recipe(List((OIL, 500)), (PETROLEUM_PRODUCT, 1000), new DateTime(15, 0))
          )
        )

      case OIL_WELLS =>
        new Factory(OIL_WELLS, company, town,
          ListBuffer(
            Recipe(List(), (OIL, 2000), new DateTime(10, 0))
          )
        )

      case PAPER_MILL =>
        new Factory(PAPER_MILL, company, town,
          ListBuffer(
            Recipe(List((WOODCHIP, 100)), (PAPER, 500), new DateTime(15, 0))
          )
        )

      case PIG_FARM =>
        new Factory(PIG_FARM, company, town,
          ListBuffer(
            Recipe(List((GRAIN, 2500)), (PIG, 100), new DateTime(20, 0)),
            Recipe(List((VEGETABLE, 1000)), (PIG, 100), new DateTime(20, 0))
          )
        )

      case PRINTING_PRESS =>
        new Factory(PRINTING_PRESS, company, town,
          ListBuffer(
            Recipe(List((PAPER, 500)), (PRESS, 100), new DateTime(14, 0))
          )
        )

      case QUARRY =>
        new Factory(QUARRY, company, town,
          ListBuffer(
            Recipe(List(), (LIMESTONE, 1000), new DateTime(10, 0)),
            Recipe(List(), (MARBLE, 1000), new DateTime(10, 0))
          )
        )

      case RUBBER_PLANTATION =>
        new Factory(RUBBER_PLANTATION, company, town,
          ListBuffer(
            Recipe(List(), (RUBBER, 1000), new DateTime(30, 0))
          )
        )

      case SAND_PIT =>
        new Factory(SAND_PIT, company, town,
          ListBuffer(
            Recipe(List(), (SAND, 1000), new DateTime(10, 0))
          )
        )

      case SHEEP_FARM =>
        new Factory(SHEEP_FARM, company, town,
          ListBuffer(
            Recipe(List((GRAIN, 2500)), (SHEEP, 100), new DateTime(20, 0)),
            Recipe(List((GRAIN, 1000)), (WOOL, 500), new DateTime(12, 0))
          )
        )

      case STEEL_MILL =>
        new Factory(STEEL_MILL, company, town,
          ListBuffer(
            Recipe(List((COAL, 500), (IRON, 1000)), (STEEL, 500), new DateTime(15, 0))
          )
        )

      case STOCKYARD =>
        new Factory(STOCKYARD, company, town,
          ListBuffer(
            Recipe(List((PIG, 10)), (MEAT, 1000), new DateTime(12, 0)),
            Recipe(List((CATTLE, 10)), (MEAT, 800), new DateTime(12, 0)),
            Recipe(List((SHEEP, 10)), (MEAT, 500), new DateTime(12, 0))
          )
        )

      case TANNERY =>
        new Factory(TANNERY, company, town,
          ListBuffer(
            Recipe(List((CATTLE, 50)), (LEATHER, 500), new DateTime(12, 0))
          )
        )

      case TEXTILE_MILL =>
        new Factory(TEXTILE_MILL, company, town,
          ListBuffer(
            Recipe(List((COTTON, 100)), (TEXTILE, 1000), new DateTime(12, 0)),
            Recipe(List((WOOL, 100)), (TEXTILE, 800), new DateTime(12, 0)),
            Recipe(List((LEATHER, 100)), (TEXTILE, 500), new DateTime(12, 0))
          )
        )

      case TYRE_FACTORY =>
        new Factory(TYRE_FACTORY, company, town,
          ListBuffer(
            Recipe(List((CHEMICAL, 100), (RUBBER, 500), (STEEL_WIRE, 250)), (TYRE, 250), new DateTime(15, 0))
          )
        )

      case VEGETABLE_FARM =>
        new Factory(VEGETABLE_FARM, company, town,
          ListBuffer(
            Recipe(List(), (VEGETABLE, 5000), new DateTime(30, 0))
          )
        )

      case VEHICLE_FACTORY =>
        new Factory(VEHICLE_FACTORY, company, town,
          ListBuffer(
            Recipe(List((ALUMINIUM, 500), (ELECTRONICS, 1000), (GLASS, 800), (PLASTIC, 1500), (STEEL, 800), (TYRE, 100)), (VEHICLE, 100), new DateTime(30, 0))
          )
        )

      case VINEYARD =>
        new Factory(VINEYARD, company, town,
          ListBuffer(
            Recipe(List((GLASS, 100)), (WINE, 200), new DateTime(30, 0))
          )
        )

      case WIRE_MILL =>
        new Factory(WIRE_MILL, company, town,
          ListBuffer(
            Recipe(List((ALUMINIUM, 800)), (ALUMINIUM_WIRE, 500), new DateTime(20, 0)),
            Recipe(List((COPPER, 800)), (COPPER_WIRE, 500), new DateTime(20, 0)),
            Recipe(List((STEEL, 800)), (STEEL_WIRE, 500), new DateTime(20, 0))
          )
        )

    }

  }

  def loadFactory (name : String, company: Company, town: Town) : Factory = { name match {
    case "Aluminium plant" => make(ALUMINIUM_PLANT, company, town)
    case "Bakery" => make(BAKERY, company, town)
    case "Bauxite mine" => make(BAUXITE_MINE, company, town)
    case "Brewery" => make(BREWERY, company, town)
    case "Brickworks" => make(BRICKWORKS, company, town)
    case "Cannery" => make(CANNERY, company, town)
    case "Cattle ranch" => make(CATTLE_RANCH, company, town)
    case "Cement factory" => make(CEMENT_FACTORY, company, town)
    case "Chemical plant" => make(CHEMICAL_PLANT, company, town)
    case "Clay pit" => make(CLAY_PIT, company, town)
    case "Coal mine" => make(COAL_MINE, company, town)
    case "Copper mine" => make(COPPER_MINE, company, town)
    case "Cotton plantation" => make(COTTON_PLANTATION, company, town)
    case "Electronics factory" => make(ELECTRONICS_FACTORY, company, town)
    case "Fishery" => make(FISHERY, company, town)
    case "Forestry" => make(FORESTRY, company, town)
    case "Fruit orchard" => make(FRUIT_ORCHARD, company, town)
    case "Furniture factory" => make(FURNITURE_FACTORY, company, town)
    case "Glass factory" => make(GLASS_FACTORY, company, town)
    case "Grain farm" => make(GRAIN_FARM, company, town)
    case "Iron mine" => make(IRON_MINE, company, town)
    case "Liquor distillery" => make(LIQUOR_DISTILLERY, company, town)
    case "Lumber mill" => make(LUMBER_MILL, company, town)
    case "Oil refinery" => make(OIL_REFINERY, company, town)
    case "Oil wells" => make(OIL_WELLS, company, town)
    case "Paper mill" => make(PAPER_MILL, company, town)
    case "Pig farm" => make(PIG_FARM, company, town)
    case "Printing press" => make(PRINTING_PRESS, company, town)
    case "Quarry" => make(QUARRY, company, town)
    case "Rubber plantation" => make(RUBBER_PLANTATION, company, town)
    case "Sand pit" => make(SAND_PIT, company, town)
    case "Sheep farm" => make(SHEEP_FARM, company, town)
    case "Steel mill" => make(STEEL_MILL, company, town)
    case "Stockyard" => make(STOCKYARD, company, town)
    case "Tannery" => make(TANNERY, company, town)
    case "Textile mill" => make(TEXTILE_MILL, company, town)
    case "Tyre factory" => make(TYRE_FACTORY, company, town)
    case "Vegetable farm" => make(VEGETABLE_FARM, company, town)
    case "Vehicle factory" => make(VEHICLE_FACTORY, company, town)
    case "Vineyard" => make(VINEYARD, company, town)
    case "Wire mill" => make(WIRE_MILL, company, town)
  }
  }

}
