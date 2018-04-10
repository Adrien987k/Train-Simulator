package logic.items.transport.vehicules.components

import logic.economy.ResourcePack
import logic.economy.Resources.{Resource, ResourceType}
import logic.items.transport.vehicules.components.VehicleComponentTypes.RESOURCE_CARRIAGE
import logic.world.Company

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class ResourceCarriage
(val resourceType : ResourceType,
 override val company : Company,
 _maxSpeed : Double,
 _weight : Double,
 private var _weightCapacity : Double)
  extends Carriage(RESOURCE_CARRIAGE, company, _maxSpeed, _weight) {

  def weightCapacity : Double = _weightCapacity

  var resources : ListBuffer[ResourcePack] = ListBuffer.empty

  def addResourcePack(resourcePack : ResourcePack) : Unit = {
    if (resourcePack.resource.resourceType != resourceType) return

    val resourcesWeight = resources.foldLeft(0.0)((total, resPack) => total + resPack.weight)
    if (resourcePack.weight + resourcesWeight > _weight) {
      //TODO Throw EXCP
    }

    resources += resourcePack
  }

  def takeResources() : ListBuffer[ResourcePack] = {
    val resourcesResult = resources

    resources = ListBuffer.empty

    resourcesResult
  }

  def resourceMap() : Map[Resource, Int] = {
    val result : mutable.HashMap[Resource, Int] = mutable.HashMap.empty

    resources.foldLeft(result)((map, pack) => {
      val oldQuantity =
        if (map.contains(pack.resource)) map(pack.resource)
        else 0

      map.update(pack.resource, pack.quantity + oldQuantity)

      map
    }).toMap
  }

}
