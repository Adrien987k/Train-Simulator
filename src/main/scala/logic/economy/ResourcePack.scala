package logic.economy

import logic.economy.Resources.{BoxedResource, DryBulkResource, LiquidResource, Resource}
import statistics.StatValue

import scala.collection.mutable.ListBuffer

class ResourcePack
(val resource : Resource,
 var quantity : Int) extends StatValue {

  private var _costOneUnit : Double = resource.defaultCost

  def costOneUnit : Double = _costOneUnit
  def costOneUnit_= (value : Double) : Unit = {
    if (resource.minCost <= value && value <= resource.maxCost)
      _costOneUnit = value
  }

  def resourceName() : String = resource.name

  def unit() : String = resource.unit

  def cost() : Double = costOneUnit * quantity

  def weight() : Double = resource.weight * quantity

  def merge(resourcePack : ResourcePack) : Unit = {
    if (resourcePack.resource == resource) {
      quantity += resourcePack.quantity
    }
  }

  override def info() : String =
    resource.name + ": " + quantity + " " + resource.unit

  override def mean(l : ListBuffer[StatValue]) : ResourcePack = this

  override def sum(v : StatValue) : ResourcePack = this
}

class DryBulkResourcePack(resourceType : DryBulkResource, quantity : Int)
  extends ResourcePack(resourceType, quantity) {

}

class LiquidResourcePack(resourceType : LiquidResource, quantity : Int)
  extends ResourcePack(resourceType, quantity) {

}

class BoxedResourcePack(resourceType : BoxedResource, quantity : Int)
  extends ResourcePack(resourceType, quantity) {

}