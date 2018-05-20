package logic.economy

import logic.economy.Resources.Resource

import scala.collection.mutable
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class ResourceMap() {

  def this(map : mutable.HashMap[Resource, Int]) {
    this()

    resources ++= map
  }

  def this(map : Map[Resource, Int]) {
    this()

    resources ++= map
  }

  var resources : mutable.HashMap[Resource, Int] = mutable.HashMap.empty

  def putMap(map : mutable.HashMap[Resource, Int]) : Unit = {
    resources = map
  }

  def addSome(resource : Resource, quantity : Int) : Unit = {
    val oldQuantity =
      if (resources.contains(resource))
        resources(resource)
      else 0

    resources.update(resource, oldQuantity + quantity)
  }

  def removeSome(resource : Resource, quantity : Int) : Unit = {
    val oldQuantity =
      if (resources.contains(resource))
        resources(resource)
      else 0

    if (oldQuantity - quantity <= 0)
      resources.remove(resource)
    else
      resources.update(resource, oldQuantity - quantity)
  }

  def removeAll(resource : Resource) : Unit = {
    removeSome(resource, Integer.MAX_VALUE)
  }

  def quantityOf(resource : Resource) : Int = {
    if (resources.contains(resource))
      resources(resource)

    else 0
  }

  def merge(otherMap : ResourceMap) : ResourceMap = {
    val result = resources.foldLeft(otherMap.resources)((map, resource_quantity) => {
      val (resource, quantity) = resource_quantity
      val oldQuantity =
        if (map.contains(resource)) map(resource)
        else 0

      map.update(resource, quantity + oldQuantity)

      map
    })

    new ResourceMap(result)
  }



  /* GUI */

  private val panel = new VBox()
  private val label = new Label()

  panel.children = List(label)

  def propertyPane() : VBox = {
    val builder : StringBuilder = new StringBuilder

    resources.foldLeft(builder)((builder, resourcesAndQuantity) => {
      val (resource, quantity) = resourcesAndQuantity

      builder.append(resource.name + " : " + quantity + " " + resource.unit + "\n")
    })

    label.text = builder.toString()

    panel
  }

}
