package logic.items.transport.vehicules

import logic.economy.ResourcesTypes.ResourceType

import scala.collection.mutable.ListBuffer

class ResourceCarriage[R <: ResourceType]
(maxSpeed : Double,
 maxWeight : Double)
  extends Carriage(maxSpeed, maxWeight) {

  var resources : ListBuffer[R] = ListBuffer.empty

  def addResourcePack(resourcePack : R) : Unit = {
    val resourcesWeight = resources.foldLeft(0.0)((total, resPack) => total + resPack.weight)
    if (resourcePack.weight + resourcesWeight > maxWeight) {
      //TODO Throw EXCP
    }
    resources += resourcePack
  }

}
