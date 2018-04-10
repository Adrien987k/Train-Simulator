package logic.items.production

import logic.economy.Resources._
import logic.items.production.FactoryTypes._
import logic.world.Company
import logic.world.towns.Town
import utils.DateTime

import scala.collection.mutable.ListBuffer

object FactoryFactory {

  def make(factoryType : FactoryType, company : Company, town : Town) : Factory = {

    factoryType match {
      case GRAIN_FARM =>
        new Factory(GRAIN_FARM, company, town,
          ListBuffer(
            Recipe(List(), (GRAIN, 5000), new DateTime(4, 0))
          )
        )

      case GARDEN =>
        new Factory(GARDEN, company, town,
          ListBuffer(
            //Recipe(List(), (VEGETABLE, 10000), new DateTime(30, 0)),
            Recipe(List(), (FRUIT, 10000), new DateTime(5, 0))
          )
        )

      case IRON_MINE =>
        new Factory(IRON_MINE, company, town,
          ListBuffer(
            Recipe(List(), (IRON, 350), new DateTime(1, 0))
          )
        )

      case BAUXITE_MINE =>
        new Factory(BAUXITE_MINE, company, town,
          ListBuffer(
            Recipe(List(), (BAUXITE, 100), new DateTime(1, 0))
          )
        )

      case COAL_MINE =>
        new Factory(COAL_MINE, company, town,
          ListBuffer(
            Recipe(List(), (COAL, 1500), new DateTime(0, 12))
          )
        )

      case TEXTILE_FACTORY =>
        new Factory(TEXTILE_FACTORY, company, town,
          ListBuffer(
            Recipe(List((IRON, 10)), (TEXTILE, 1), new DateTime(3, 0)),
            Recipe(List((BAUXITE, 5)), (TEXTILE, 1), new DateTime(3, 0)),
            Recipe(List((BAUXITE, 10)), (LEATHER, 1), new DateTime(3, 0)),
            Recipe(List((LEATHER, 3)), (PAPER, 1), new DateTime(3, 0))
          )
        )

    }

  }

}
