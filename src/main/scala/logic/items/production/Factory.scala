package logic.items.production

import logic.items.{EvolutionPlan, Facility}
import logic.items.production.FactoryTypes.FactoryType
import logic.world.Company
import logic.world.towns.Town

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label

class Factory
(val factoryType : FactoryType,
 override val company : Company,
 override val town : Town,
 val recipes : ListBuffer[Recipe] = ListBuffer.empty)
  extends Facility(factoryType, company, town,
    new EvolutionPlan(List(), 0.0, 0.0)) {

  val productions : ListBuffer[Production] = ListBuffer.empty

  override def step() : Boolean = {

    recipes.foreach(recipe => {
      val packs =
        if (recipe.input.nonEmpty && town.warehouse.available(recipe.input))
          town.warehouse.takeSeveral(recipe.input)
        else List()

      val production = new Production(recipe)
      production.start(packs)

      stats.newEvent("Start Production", recipe)

      productions += production
    })

    productions.foreach(production => {
      production.checkIsFinished match {
        case Some(pack) =>
          stats.newEvent("Pack produced", pack)

          town.warehouse.storeResourcePack(pack)
          productions -= production

        case None =>
      }
    })

    true
  }

  /* GUI */

  val factoryLabel = new Label("=== " + factoryType.name + " ===")
  val producingLabel = new Label
  val levelLabel = new Label

  labels = List(factoryLabel, producingLabel, levelLabel)
  panel.children.addAll(factoryLabel, producingLabel, levelLabel, statsButton)

  styleLabels(14)

  override def propertyPane() : Node = {
    if (productions.nonEmpty)
      producingLabel.text = "Currently producing"
    else producingLabel.text = ""

    levelLabel.text = "Level : " + level

    panel
  }
}