package logic.items.production

import logic.items.Facility
import logic.items.production.FactoryTypes.FactoryType
import logic.world.Company
import logic.world.towns.Town

import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

class Factory
(val factoryType : FactoryType,
 override val company : Company,
 override val town : Town,
 val recipes : ListBuffer[Recipe] = ListBuffer.empty)
  extends Facility(factoryType, company, town) {

  val productions : ListBuffer[Production] = ListBuffer.empty

  override def step() : Boolean = {

    recipes.foreach(recipe => {
      if (recipe.input.nonEmpty) {
        if (town.warehouse.available(recipe.input)) {
          val packs =  town.warehouse.takeAll(recipe.input)

          val production = new Production(recipe)
          production.start(packs)

          productions += production
        }

      } else {
        val production = new Production(recipe)
        production.start()

        productions += production
      }
    })

    productions.foreach(production => {
      production.checkIsFinished match {
        case Some(pack) =>
          println("Pack produces")
          town.warehouse.storeResourcePack(pack)
          productions -= production

        case None =>
      }
    })

    true
  }

  override def evolve() : Unit = {

  }

  /* GUI */

  val panel = new VBox()
  val factoryLabel = new Label("=== " + factoryType.name + " ===")
  val producingLabel = new Label()

  labels = List(factoryLabel, producingLabel)

  panel.children = labels

  styleLabels(14)

  override def propertyPane(): Node = {
    if (productions.nonEmpty)
      producingLabel.text = "Currently producing"
    else producingLabel.text = ""

    panel
  }
}