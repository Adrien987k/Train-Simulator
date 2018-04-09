package logic.items.production

import logic.economy.Resources.{COAL, COTTON, FUEL, GRAIN}
import utils.DateTime

import scala.collection.mutable.ListBuffer

object Recipes {

  def makeBasicRecipes() : ListBuffer[Recipe] = {
    ListBuffer(

      Recipe(Some((COAL, 20)), (FUEL, 50), new DateTime(1, 0, 0)),
      Recipe(Some((GRAIN, 100)), (COTTON, 25), new DateTime(0, 12, 0))

    )
  }

  //TODO Advanced recipes
  /*
  def makeAdvancedRecipes() : ListBuffer[Recipe] = {
    ListBuffer(

      new Recipe(),
      new Recipe()

    )
  }
  */

}
