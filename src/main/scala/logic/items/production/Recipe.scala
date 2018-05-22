package logic.items.production

import logic.economy.Resources.Resource
import statistics.StatValue
import utils.DateTime

import scala.collection.mutable.ListBuffer

case class Recipe
(input : List[(Resource, Int)],
 output : (Resource, Int),
 time : DateTime) extends StatValue {

  override def info() : String =
    output._1.name + " quantity: " + output._2

  override def average(l : ListBuffer[StatValue]) : StatValue = l.head

  override def sum(v : StatValue) : StatValue = v
}