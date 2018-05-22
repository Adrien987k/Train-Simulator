package logic.economy

object Resources {

  sealed abstract class ResourceType extends Comparable[ResourceType] {
    override def compareTo(o: ResourceType): Int = {
      if (this.getClass.getName.equals(o.getClass.getName)) 0 else -1
    }
  }

  case object DRY_BULK extends ResourceType

  case object LIQUID extends ResourceType

  case object BOXED extends ResourceType

  sealed abstract class Resource
  (val resourceType: ResourceType,
   val name: String,
   val unit: String,
   val defaultCost: Double,
   val minCost: Double,
   val maxCost: Double,
   val weight: Double) {

  }

  sealed abstract class DryBulkResource
  (name: String, defaultCost: Double, minCost: Double, maxCost: Double, weight: Double)
    extends Resource(DRY_BULK, name, "Liter", defaultCost, minCost, maxCost, weight) {}

  sealed abstract class LiquidResource
  (name: String, defaultCost: Double, minCost: Double, maxCost: Double, weight: Double)
    extends Resource(LIQUID, name, "Liter", defaultCost, minCost, maxCost, weight) {}

  sealed abstract class BoxedResource
  (name: String, unit: String, defaultCost: Double, minCost: Double, maxCost: Double, weight: Double,
   size: Int)
    extends Resource(BOXED, name, unit, defaultCost, minCost, maxCost, weight) {}

  case object ALUMINIUM extends BoxedResource("aluminium", "box", 50.0, 25.0, 100.0, 10.0, 5)

  case object ALUMINIUM_WIRE extends BoxedResource("aluminium wire", "box", 100.0, 50.0, 200.0, 5.0, 10)

  case object BAKED_GOOD extends BoxedResource("baked good", "box", 30.0, 10.0, 90.0, 5.0, 3)

  case object BAUXITE extends BoxedResource("bauxite", "box", 30.0, 15.0, 60.0, 10.0, 10)

  case object BEER extends LiquidResource("beer", 50.0, 30.0, 60.0, 10.0)

  case object BRICK extends BoxedResource("brick", "box", 10.0, 5.0, 20.0, 50.0, 10)

  case object CANNED_FOOD extends BoxedResource("Canned food", "box", 20.0, 5.0, 100.0, 10.0, 2)

  case object CATTLE extends BoxedResource("cattle", "individual", 100.0, 20.0, 200.0, 10.0, 5)

  case object CEMENT extends DryBulkResource("cement", 20.0, 10.0, 40.0, 15.0)

  case object CHEMICAL extends LiquidResource("chemical", 80.0, 20.0, 200.0, 3.0)

  case object CLAY extends BoxedResource("clay", "unit", 30.0, 15.0, 60.0, 10.0, 5)

  case object COAL extends DryBulkResource("coal", 50.0, 10.0, 200.0, 10.0)

  case object COPPER extends DryBulkResource("copper", 60.0, 30.0, 120.0, 15.0)

  case object COPPER_WIRE extends BoxedResource("copper wire", "box", 120.0, 60.0, 240.0, 10.0, 10)

  case object COTTON extends DryBulkResource("cotton", 50.0, 20.0, 100.0, 2.0)

  case object ELECTRONICS extends BoxedResource("electronics", "box", 50.0, 25.0, 100.0, 10.0, 20)

  case object FISH extends BoxedResource("fish", "box", 50.0, 10.0, 120.0, 10.0, 15)

  case object FRUIT extends BoxedResource("fruit", "box", 50.0, 10.0, 150.0, 10.0, 15)

  case object FUEL extends LiquidResource("fuel", 50.0, 10.0, 200.0, 10.0)

  case object FURNITURE extends BoxedResource("furniture", "individual", 20.0, 15.0, 40.0, 10.0, 20)

  case object GLASS extends BoxedResource("glass", "box", 50.0, 25.0, 100.0, 5.0, 10)

  case object GRAIN extends DryBulkResource("grain", 10.0, 5.0, 20.0, 2.0)

  case object IRON extends DryBulkResource("iron", 50.0, 20.0, 150.0, 20.0)

  case object LEATHER extends BoxedResource("leather", "box", 100.0, 50.0, 200.0, 10.0, 20)

  case object LIMESTONE extends DryBulkResource("limestone", 40.0, 20.0, 80.0, 10.0)

  case object LIQUOR extends LiquidResource("liquor", 100.0, 40.0, 200.0, 5.0)

  case object LUMBER extends DryBulkResource("lumber", 50.0, 25.0, 100.0, 15.0)

  case object MARBLE extends DryBulkResource("marble", 100.0, 80.0, 150.0, 20.0)

  case object MEAT extends BoxedResource("meat", "box", 100.0, 10.0, 300.0, 15.0, 25)

  case object MILK extends LiquidResource("milk", 50.0, 10.0, 150.0, 5.0)

  case object OIL extends LiquidResource("oil", 20.0, 5.0, 100.0, 10.0)

  case object PAPER extends DryBulkResource("paper", 80.0, 40.0, 160.0, 5.0)

  case object PETROLEUM_PRODUCT extends LiquidResource("petroleum product", 50.0, 10.0, 100.0, 15.0)

  case object PIG extends BoxedResource("pig", "individual", 80.0, 40.0, 160.0, 10.0, 5)

  case object PLASTIC extends DryBulkResource("plastic", 50.0, 10.0, 150.0, 5)

  case object PRESS extends BoxedResource("press", "box", 40.0, 1.0, 50.0, 5.0, 5)

  case object RUBBER extends DryBulkResource("rubber", 50.0, 25.0, 100.0, 5.0)

  case object SAND extends DryBulkResource("sand", 50.0, 20.0, 120.0, 15.0)

  case object SHEEP extends BoxedResource("sheep", "individual", 80.0, 40.0, 160.0, 10.0, 5)

  case object STEEL extends DryBulkResource("steel", 80.0, 40.0, 160.0, 12.0)

  case object STEEL_WIRE extends BoxedResource("steel wire", "box", 100.0, 80.0, 200.0, 10.0, 15)

  case object TEXTILE extends BoxedResource("textile", "box", 50.0, 25.0, 100.0, 5.0, 5)

  case object TIMBER extends BoxedResource("timber", "individual", 20.0, 10.0, 30.0, 2.0, 5)

  case object TYRE extends BoxedResource("tyre", "individual", 10.0, 5.0, 20.0, 2.0, 1)

  case object VEGETABLE extends BoxedResource("vegetable", "box", 50.0, 10.0, 200.0, 10.0, 10)

  case object VEHICLE extends BoxedResource("vehicle", "individual", 100.0, 50.0, 200.0, 5, 30)

  case object WINE extends LiquidResource("wine", 150.0, 100.0, 300.0, 10.0)

  case object WOODCHIP extends DryBulkResource("woodchip", 10.0, 5.0, 30.0, 5)

  case object WOOL extends DryBulkResource("wool", 50.0, 20.0, 100.0, 5.0)

  def resources(): List[Resource with Product with Serializable] = {
    List(ALUMINIUM, BAUXITE, COAL, GRAIN, BAKED_GOOD, SHEEP, WOOL, TEXTILE)
  }

  def loadResource(name: String, quantity: Int): ResourcePack = {
    name match {
      case "aluminium" => new ResourcePack(ALUMINIUM, quantity)
      case "aluminium wire" => new ResourcePack(ALUMINIUM_WIRE, quantity)
      case "baked good" => new ResourcePack(BAKED_GOOD, quantity)
      case "bauxite" => new ResourcePack(BAUXITE, quantity)
      case "beer" => new ResourcePack(BEER, quantity)
      case "brick" => new ResourcePack(BRICK, quantity)
      case "Canned food" => new ResourcePack(CANNED_FOOD, quantity)
      case "cattle" => new ResourcePack(CATTLE, quantity)
      case "cement" => new ResourcePack(CEMENT, quantity)
      case "chemical" => new ResourcePack(CHEMICAL, quantity)
      case "clay" => new ResourcePack(CLAY, quantity)
      case "coal" => new ResourcePack(COAL, quantity)
      case "copper" => new ResourcePack(COPPER, quantity)
      case "copper wire" => new ResourcePack(COPPER_WIRE, quantity)
      case "cotton" => new ResourcePack(COTTON, quantity)
      case "electronics" => new ResourcePack(ELECTRONICS, quantity)
      case "fish" => new ResourcePack(FISH, quantity)
      case "fruit" => new ResourcePack(FRUIT, quantity)
      case "fuel" => new ResourcePack(FUEL, quantity)
      case "furniture" => new ResourcePack(FURNITURE, quantity)
      case "glass" => new ResourcePack(GLASS, quantity)
      case "grain" => new ResourcePack(GRAIN, quantity)
      case "iron" => new ResourcePack(IRON, quantity)
      case "leather" => new ResourcePack(LEATHER, quantity)
      case "limestone" => new ResourcePack(LIMESTONE, quantity)
      case "liquor" => new ResourcePack(LIQUOR, quantity)
      case "lumber" => new ResourcePack(LUMBER, quantity)
      case "marble" => new ResourcePack(MARBLE, quantity)
      case "meat" => new ResourcePack(MEAT, quantity)
      case "milk" => new ResourcePack(MILK, quantity)
      case "oil" => new ResourcePack(OIL, quantity)
      case "paper" => new ResourcePack(PAPER, quantity)
      case "petroleum product" => new ResourcePack(PETROLEUM_PRODUCT, quantity)
      case "pig" => new ResourcePack(PIG, quantity)
      case "plastic" => new ResourcePack(PLASTIC, quantity)
      case "press" => new ResourcePack(PRESS, quantity)
      case "rubber" => new ResourcePack(RUBBER, quantity)
      case "sand" => new ResourcePack(SAND, quantity)
      case "sheep" => new ResourcePack(SHEEP, quantity)
      case "steel" => new ResourcePack(STEEL, quantity)
      case "steel wire" => new ResourcePack(STEEL_WIRE, quantity)
      case "textile" => new ResourcePack(TEXTILE, quantity)
      case "timber" => new ResourcePack(TIMBER, quantity)
      case "tyre" => new ResourcePack(TYRE, quantity)
      case "vegetable" => new ResourcePack(VEGETABLE, quantity)
      case "vehicle" => new ResourcePack(VEHICLE, quantity)
      case "wine" => new ResourcePack(WINE, quantity)
      case "woodchip" => new ResourcePack(WOODCHIP, quantity)
      case "wool" => new ResourcePack(WOOL, quantity)
    }
  }

  def addResourceMap(resourceMap: ResourceMap, name: String, quantity: Int): Unit = {
    name match {
      case "aluminium" => resourceMap.addSome(ALUMINIUM, quantity)
      case "aluminium wire" => resourceMap.addSome(ALUMINIUM_WIRE, quantity)
      case "baked good" => resourceMap.addSome(BAKED_GOOD, quantity)
      case "bauxite" => resourceMap.addSome(BAUXITE, quantity)
      case "beer" => resourceMap.addSome(BEER, quantity)
      case "brick" => resourceMap.addSome(BRICK, quantity)
      case "Canned food" => resourceMap.addSome(CANNED_FOOD, quantity)
      case "cattle" => resourceMap.addSome(CATTLE, quantity)
      case "cement" => resourceMap.addSome(CEMENT, quantity)
      case "chemical" => resourceMap.addSome(CHEMICAL, quantity)
      case "clay" => resourceMap.addSome(CLAY, quantity)
      case "coal" => resourceMap.addSome(COAL, quantity)
      case "copper" => resourceMap.addSome(COPPER, quantity)
      case "copper wire" => resourceMap.addSome(COPPER_WIRE, quantity)
      case "cotton" => resourceMap.addSome(COTTON, quantity)
      case "electronics" => resourceMap.addSome(ELECTRONICS, quantity)
      case "fish" => resourceMap.addSome(FISH, quantity)
      case "fruit" => resourceMap.addSome(FRUIT, quantity)
      case "fuel" => resourceMap.addSome(FUEL, quantity)
      case "furniture" => resourceMap.addSome(FURNITURE, quantity)
      case "glass" => resourceMap.addSome(GLASS, quantity)
      case "grain" => resourceMap.addSome(GRAIN, quantity)
      case "iron" => resourceMap.addSome(IRON, quantity)
      case "leather" => resourceMap.addSome(LEATHER, quantity)
      case "limestone" => resourceMap.addSome(LIMESTONE, quantity)
      case "liquor" => resourceMap.addSome(LIQUOR, quantity)
      case "lumber" => resourceMap.addSome(LUMBER, quantity)
      case "marble" => resourceMap.addSome(MARBLE, quantity)
      case "meat" => resourceMap.addSome(MEAT, quantity)
      case "milk" => resourceMap.addSome(MILK, quantity)
      case "oil" => resourceMap.addSome(OIL, quantity)
      case "paper" => resourceMap.addSome(PAPER, quantity)
      case "petroleum product" => resourceMap.addSome(PETROLEUM_PRODUCT, quantity)
      case "pig" => resourceMap.addSome(PIG, quantity)
      case "plastic" => resourceMap.addSome(PLASTIC, quantity)
      case "press" => resourceMap.addSome(PRESS, quantity)
      case "rubber" => resourceMap.addSome(RUBBER, quantity)
      case "sand" => resourceMap.addSome(SAND, quantity)
      case "sheep" => resourceMap.addSome(SHEEP, quantity)
      case "steel" => resourceMap.addSome(STEEL, quantity)
      case "steel wire" => resourceMap.addSome(STEEL_WIRE, quantity)
      case "textile" => resourceMap.addSome(TEXTILE, quantity)
      case "timber" => resourceMap.addSome(TIMBER, quantity)
      case "tyre" => resourceMap.addSome(TYRE, quantity)
      case "vegetable" => resourceMap.addSome(VEGETABLE, quantity)
      case "vehicle" => resourceMap.addSome(VEHICLE, quantity)
      case "wine" => resourceMap.addSome(WINE, quantity)
      case "woodchip" => resourceMap.addSome(WOODCHIP, quantity)
      case "wool" => resourceMap.addSome(WOOL, quantity)
    }

    def resources(): List[Resource] = {
      List(
        ALUMINIUM,
        ALUMINIUM_WIRE,
        BAKED_GOOD,
        BAUXITE,
        BEER,
        BRICK,
        CANNED_FOOD,
        CATTLE,
        CEMENT,
        CHEMICAL,
        CLAY,
        COAL,
        COPPER,
        COPPER_WIRE,
        COTTON,
        ELECTRONICS,
        FISH,
        FRUIT,
        FUEL,
        FURNITURE,
        GLASS,
        GRAIN,
        IRON,
        LEATHER,
        LIMESTONE,
        LIQUOR,
        LUMBER,
        MARBLE,
        MEAT,
        MILK,
        OIL,
        PAPER,
        PETROLEUM_PRODUCT,
        PIG,
        PLASTIC,
        PRESS,
        RUBBER,
        SAND,
        SHEEP,
        STEEL,
        STEEL_WIRE,
        TEXTILE,
        TIMBER,
        TYRE,
        VEGETABLE,
        VEHICLE,
        WINE,
        WOODCHIP,
        WOOL)

    }
  }
}


