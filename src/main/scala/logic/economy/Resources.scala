package logic.economy

object Resources {

  sealed abstract class ResourceType extends Comparable[ResourceType] {
    override def compareTo(o: ResourceType): Int = {
      if(this.getClass.getName.equals(o.getClass.getName)) 0 else -1
    }
  }

  case object DRY_BULK extends ResourceType
  case object LIQUID extends ResourceType
  case object BOXED extends ResourceType

  sealed abstract class Resource
  (val resourceType : ResourceType,
   val name : String,
   val unit : String,
   val defaultCost : Double,
   val minCost : Double,
   val maxCost : Double,
   val weight : Double) {

  }

  sealed abstract class DryBulkResource
  (name : String, defaultCost: Double, minCost : Double, maxCost: Double, weight : Double)
    extends Resource(DRY_BULK, name, "Liter", defaultCost, minCost, maxCost, weight)
  {}

  sealed abstract class LiquidResource
  (name : String, defaultCost : Double, minCost : Double, maxCost : Double, weight : Double)
    extends Resource(LIQUID, name, "Liter", defaultCost, minCost, maxCost, weight)
  {}

  sealed abstract class BoxedResource
  (name : String, unit : String, defaultCost: Double, minCost : Double, maxCost: Double, weight : Double,
   size : Int)
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

  def resources() : List[Resource] = {
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
