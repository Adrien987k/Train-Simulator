package logic.economy

import logic.economy.Resources.Resource

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class ResourceCollection(name : String) {

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
      if (pack.resource.getClass.getTypeName
        .equals(resourceType.getClass.getTypeName))
        total + pack.quantity
      else total
    })
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

  private val panel = new VBox()
  private val label = new Label()

  panel.children = List(label)

  def propertyPane() : VBox = {
    val builder : StringBuilder = new StringBuilder

    resourceMap().foldLeft(builder)((builder, resourceAndQuantity) => {
      val (resource, quantity) = resourceAndQuantity

      builder.append(resource.name + " : " + quantity + " " + resource.unit + "\n")
    })

    label.text = "=== " + name + " ===\n" + builder.toString()

    panel
  }

}
