package logic.items.transport.vehicules

import logic.economy.ResourcesTypes.ResourceType
import logic.items.transport.vehicules.VehicleComponentTypes.RESOURCE_CARRIAGE

import scala.collection.mutable.ListBuffer

class ResourceCarriage[R <: ResourceType]
(maxSpeed : Double,
 maxWeight : Double)
  extends Carriage(RESOURCE_CARRIAGE, maxSpeed, maxWeight) {

  var resources : ListBuffer[R] = ListBuffer.empty

  def addResourcePack(resourcePack : R) : Unit = {
    val resourcesWeight = resources.foldLeft(0.0)((total, resPack) => total + resPack.weight)
    if (resourcePack.weight + resourcesWeight > maxWeight) {
      //TODO Throw EXCP
    }
    resources += resourcePack
  }

}
