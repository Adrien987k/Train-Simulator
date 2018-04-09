package logic.economy

import logic.economy.Resources.Resource

import scala.collection.mutable

class TownRequests {

  val requests : mutable.HashMap[Resource, Int] = mutable.HashMap.empty

  def update(resourceType: Resource, quantity : Int) : Unit = {
    requests.update(resourceType, quantity)
  }

  def remove(resourceType : Resource) : Unit = {
    if (requests.contains(resourceType))
      requests.remove(resourceType)
  }

}
