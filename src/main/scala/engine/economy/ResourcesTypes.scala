package engine.economy

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
  (name : String, unit : String, defaultCost: Int, minCost : Double, maxCost: Double, weight : Double)
    extends ResourceType(name, "Liter", defaultCost, minCost, maxCost, weight)
  {}

  sealed abstract class LiquidResourceType
  (name : String, defaultCost : Double, minCost : Double, maxCost : Double, weight : Double)
    extends ResourceType(name, "Liter", defaultCost, minCost, maxCost, weight)
  {}

  sealed abstract class BoxedResourceType
  (name : String, unit : String, defaultCost: Int, minCost : Double, maxCost: Double, weight : Double,
   size : Int)
    extends ResourceType(name, unit, defaultCost, minCost, maxCost, weight) {}

  case object WOOD extends BoxedResourceType("wood", "Box", 5, 2, 10, 10, 50)

}
