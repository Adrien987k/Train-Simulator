package logic.economy

import logic.economy.ResourcesTypes.{BoxedResourceType, DryBulkResourceType, LiquidResourceType, ResourceType}

abstract class ResourcePack(resourceType : ResourceType, quantity : Int) {

  private var _costOneUnit : Double = resourceType.defaultCost

  def costOneUnit : Double = _costOneUnit
  def costOneUnit_= (value : Double) : Unit = {
    if (resourceType.minCost <= value && value <= resourceType.maxCost)
      _costOneUnit = value
  }

  def resourceName() : String = resourceType.name

  def unit() : String = resourceType.unit

  def cost() : Double = costOneUnit * quantity

  def weight() : Double = resourceType.weight * quantity

}

class DryBulkResourcePack(resourceType : DryBulkResourceType, quantity : Int)
  extends ResourcePack(resourceType, quantity) {

}

class LiquidResourcePack(resourceType : LiquidResourceType, quantity : Int)
  extends ResourcePack(resourceType, quantity) {

}

class BoxedResourcePack(resourceType : BoxedResourceType, quantity : Int)
  extends ResourcePack(resourceType, quantity) {

}