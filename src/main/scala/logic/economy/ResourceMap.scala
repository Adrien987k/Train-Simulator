package logic.economy

import logic.economy.Resources.Resource

import scala.collection.mutable
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class ResourceMap(name : String) {

  val resources : mutable.HashMap[Resource, Int] = mutable.HashMap.empty

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

  def quantityOf(resource : Resource) : Int = {
    if (resources.contains(resource))
      resources(resource)

    else 0
  }

  private val panel = new VBox()
  private val label = new Label()

  panel.children = List(label)

  def propertyPane() : VBox = {
    val builder : StringBuilder = new StringBuilder

    resources.foldLeft(builder)((builder, resourcesAndQuantity) => {
      val (resource, quantity) = resourcesAndQuantity

      builder.append(resource.name + " : " + quantity + " " + resource.unit + "\n")
    })

    label.text = "=== " + name + " ===\n" + builder.toString()

    panel
  }

}
