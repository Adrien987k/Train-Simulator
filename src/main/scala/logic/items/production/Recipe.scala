package logic.items.production

import logic.economy.Resources.Resource
import statistics.StatValue
import utils.DateTime

case class Recipe
(input : List[(Resource, Int)],
 output : (Resource, Int),
 time : DateTime) extends StatValue {

  override def info() : String =
    output._1.name + " quantity: " + output._2

}