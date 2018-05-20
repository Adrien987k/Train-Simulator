package logic.economy

import logic.economy.Resources.Resource
import logic.world.towns.Town
import statistics.StatValue

case class Request
(town : Town,
 resource : Resource,
 quantity : Int) extends StatValue {

  override def info() : String =
    town.name + " need " + resource.name + " " + quantity + " " + resource.unit

}
