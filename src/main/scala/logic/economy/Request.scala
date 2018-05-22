package logic.economy

import logic.economy.Resources.Resource
import logic.world.towns.Town
import statistics.StatValue

import scala.collection.mutable.ListBuffer

case class Request
(town : Town,
 resource : Resource,
 quantity : Int) extends StatValue {

  override def info() : String =
    town.name + " need " + resource.name + " " + quantity + " " + resource.unit

  override def average(l : ListBuffer[StatValue]) : StatValue = l.head

  override def sum(v : StatValue) : StatValue = v
}
