package logic.items.transport.vehicules.components

import logic.economy.ResourcesTypes.ResourceType
import logic.items.transport.vehicules.components.VehicleComponentTypes.RESOURCE_CARRIAGE
import logic.world.Company

import scala.collection.mutable.ListBuffer

class ResourceCarriage[R <: ResourceType]
(override val company : Company,
  _maxSpeed : Double,
 _weight : Double,
 private var _weightCapacity : Double)
  extends Carriage(RESOURCE_CARRIAGE, company, _maxSpeed, _weight) {

  def weightCapacity : Double = _weightCapacity

  var resources : ListBuffer[R] = ListBuffer.empty

  def addResourcePack(resourcePack : R) : Unit = {
    val resourcesWeight = resources.foldLeft(0.0)((total, resPack) => total + resPack.weight)
    if (resourcePack.weight + resourcesWeight > _weight) {
      //TODO Throw EXCP
    }
    resources += resourcePack
  }

}
