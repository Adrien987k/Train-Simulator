package logic.economy

import scala.collection.mutable.ListBuffer

class Warehouse {

  val dryBulkContainers : ListBuffer[DryBulkResourcePack] = ListBuffer.empty

  val liquidContainers : ListBuffer[LiquidResourcePack] = ListBuffer.empty

  val boxedContainers : ListBuffer[BoxedResourcePack] = ListBuffer.empty

}
