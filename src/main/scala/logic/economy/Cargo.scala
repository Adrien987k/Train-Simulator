package logic.economy

import logic.economy.Resources.{Resource, ResourceType}

import scala.collection.mutable.ListBuffer

class Cargo
(val resourceType : ResourceType) {

  val resources : ResourceCollection = new ResourceCollection()

  def totalWeight : Double = {
    resources.totalWeight
  }

  def store(packs : ListBuffer[ResourcePack]) : Unit = {
    val packsToStore = packs.filter(pack => {
      pack.resource.resourceType.compareTo(resourceType) == 0
    })

    resources.storeResourcePacks(packsToStore)
  }

  def takeAll() : List[ResourcePack] = {
    resources.takeAll()
  }

  def resourceMap() : ResourceMap = resources.resourceMap()

}
