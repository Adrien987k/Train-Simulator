package logic.items.production

import logic.economy.Resources.Resource
import utils.DateTime

case class Recipe
(input : List[(Resource, Int)],
 output : (Resource, Int),
 time : DateTime) {

}