package logic.economy

import logic.economy.Resources.Resource

import scala.collection.mutable.ListBuffer

class ResourceCollection {

  val resources : ListBuffer[ResourcePack] = ListBuffer.empty

  def storeResourcePacks(resourcePacks : ListBuffer[ResourcePack]) : Unit = {
    resources ++= resourcePacks
  }

  def storeResourcePack(resourcePack : ResourcePack) : Unit = {
    resources += resourcePack
  }

  def take(resourceType : Resource, quantity : Int) : (ResourcePack , Int) = {
    var remainingToTake = quantity

    resources.foreach(resource => {
      if (resource.resource == resourceType && remainingToTake > 0) {
        if (remainingToTake <= resource.quantity) {
          resource.quantity -= remainingToTake
          remainingToTake = 0
        }

        remainingToTake -= resource.quantity
      }
    })

    resources.foreach(resource => {
      if (resource.quantity == 0) resources -= resource
    })

    (new ResourcePack(resourceType, quantity - remainingToTake),
     remainingToTake)
  }

  def takeAll(resources : List[(Resource, Int)]) : List[ResourcePack] = {
    val resultPacks : ListBuffer[ResourcePack] = ListBuffer.empty

    resources.foldLeft(resultPacks)((packs, resourceAndQuantity) => {
      val (pack, _) = take(resourceAndQuantity._1, resourceAndQuantity._2)

      packs += pack

      packs
    }).toList
  }

  def available(resources : List[(Resource, Int)]) : Boolean = {
    val lack = resources.find(resourcesAndQuantity => {
      val (resource, quantity) = resourcesAndQuantity

      quantityOf(resource) < quantity
    })

    lack.isEmpty
  }

  def quantityOf(resourceType : Resource) : Int = {
    resources.foldLeft(0)((total, pack) => {
      if (pack.resource == resourceType)
        total + pack.quantity
      else total
    })
  }

}
