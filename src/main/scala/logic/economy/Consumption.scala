package logic.economy

import logic.economy.Resources._

import scala.collection.mutable

/* Consumption for one citizen */

object Consumption {

  def initialConsumption() : Consumption = {
    val consumption = new Consumption

    consumption.addResource(VEGETABLE, 1)
    consumption.addResource(GRAIN, 5)
    consumption.addResource(FRUIT, 2)
    consumption.addResource(FUEL, 50)
    consumption.addResource(COTTON, 10)
    consumption.addResource(COAL, 5)

    consumption
  }

}

class Consumption {

  val resources : mutable.HashMap[Resource, Int] = mutable.HashMap.empty

  def addResource(resourceType : Resource, quantity : Int) : Unit =
    resources += ((resourceType, quantity))

  def removeResource(resourceType : Resource) : Unit = {
    if (resources.contains(resourceType))
      resources.remove(resourceType)
  }

  def newQuantityForResource(resourceType : Resource, newQuantity : Int) : Unit = {
    if (newQuantity == 0 && resources.contains(resourceType))
      resources.remove(resourceType)

    if (resources.contains(resourceType))
      resources.update(resourceType, newQuantity)
  }

}
