package logic.economy

object ResourcesTypes {

  abstract class ResourceType
  (val name : String,
   val unit : String,
   val defaultCost : Double,
   val minCost : Double,
   val maxCost : Double,
   val weight : Double) {

  }

  sealed abstract class DryBulkResourceType
  (name : String, defaultCost: Double, minCost : Double, maxCost: Double, weight : Double)
    extends ResourceType(name, "Liter", defaultCost, minCost, maxCost, weight)
  {}

  sealed abstract class LiquidResourceType
  (name : String, defaultCost : Double, minCost : Double, maxCost : Double, weight : Double)
    extends ResourceType(name, "Liter", defaultCost, minCost, maxCost, weight)
  {}

  sealed abstract class BoxedResourceType
  (name : String, unit : String, defaultCost: Double, minCost : Double, maxCost: Double, weight : Double,
   size : Int)
    extends ResourceType(name, unit, defaultCost, minCost, maxCost, weight) {}

  case object GRAIN extends DryBulkResourceType("gold", 10.0, 5.0, 20.0, 2.0)

  case object WATER extends LiquidResourceType("water", 1.0, 0.5, 5.0, 5.0)

  case object WOOD extends BoxedResourceType("wood", "Box", 5.0, 2.0, 10.0, 10.0, 50)

}
