package logic.economy

import logic.economy.Resources.Resource

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.layout.VBox

class ResourceCollection() {

  private val packs : ListBuffer[ResourcePack] = ListBuffer.empty

  def storeResourcePacks(resourcePacks : ListBuffer[ResourcePack]) : Unit = {
    packs ++= resourcePacks
  }

  def storeResourcePack(resourcePack : ResourcePack) : Unit = {
    packs += resourcePack
  }

  def take(resourceType : Resource, quantity : Int) : (ResourcePack , Int) = {
    var remainingToTake = quantity

    packs.foreach(pack => {
      if (pack.resource == resourceType && remainingToTake > 0) {
        println("PASS : " + pack.resource.name)
        if (remainingToTake <= pack.quantity) {
          pack.quantity -= remainingToTake
          remainingToTake = 0
        }

        remainingToTake -= pack.quantity
      }
    })

    packs.foreach(resource => {
      if (resource.quantity == 0) packs -= resource
    })

    (new ResourcePack(resourceType, quantity - remainingToTake),
     remainingToTake)
  }

  def takeSeveral(resources : List[(Resource, Int)]) : List[ResourcePack] = {
    val resultPacks : ListBuffer[ResourcePack] = ListBuffer.empty

    resources.foldLeft(resultPacks)((packs, resourceAndQuantity) => {
      val (pack, _) = take(resourceAndQuantity._1, resourceAndQuantity._2)

      packs += pack

      packs
    }).toList
  }

  def takeAll() : List[ResourcePack] = {
    val result : ListBuffer[ResourcePack] = ListBuffer.empty
    packs.copyToBuffer(result)
    packs.clear()
    result.toList
  }

  def available(resources : List[(Resource, Int)]) : Boolean = {
    val lack = resources.find(resourcesAndQuantity => {
      val (resource, quantity) = resourcesAndQuantity

      quantityOf(resource) < quantity
    })

    lack.isEmpty
  }

  def quantityOf(resourceType : Resource) : Int = {
    packs.foldLeft(0)((total, pack) => {
      if (pack.resource.getClass.getTypeName
        .equals(resourceType.getClass.getTypeName))
        total + pack.quantity
      else total
    })
  }

  def totalWeight : Double = {
    packs.foldLeft(0.0)((total, pack) => {
      total + pack.weight()
    })
  }

  private val resMap = new ResourceMap

  def resourceMap() : ResourceMap = {
    val result : mutable.HashMap[Resource, Int] = mutable.HashMap.empty

    val map = packs.foldLeft(result)((map, pack) => {
      val oldQuantity =
        if (map.contains(pack.resource)) map(pack.resource)
        else 0

      map.update(pack.resource, pack.quantity + oldQuantity)

      map
    })

    resMap.putMap(map)
    resMap
  }

  def propertyPane() : VBox = {
    resourceMap().propertyPane()
  }

}
