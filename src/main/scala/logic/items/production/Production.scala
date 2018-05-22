package logic.items.production

import game.Game
import logic.Loadable
import logic.economy.ResourcePack
import utils.DateTime

import scala.xml.Node

class Production(val recipe : Recipe) extends Loadable{

  var startTime : DateTime = _

  override def load(node: Node): Unit = {

  }

  override def save: Node = {
    <Erreur></Erreur>
  }

  def save (factory: Factory) : Node = {
      <Production
      recipe={factory.recipes.indexOf(this.recipe).toString}
      startday={this.startTime.days.toString}
      starthour={this.startTime.hours.toString}
      />
  }

  def start(input : List[ResourcePack]) : Unit = {
    //TODO Check input = recipe.input

    startTime = Game.world.time()
  }

  def start() : Unit = {
    if (recipe.input.nonEmpty) return

    startTime = Game.world.time()
  }

  def checkIsFinished : Option[ResourcePack] = {
    /*println("START TIME : " + startTime)
    println("RECIPE TIME : " + recipe.time)
    println("SUM : " + DateTime.sum(startTime, recipe.time))
    println("WORLD TIME : " + Game.world.gameDateTime + "\n")*/

    if (DateTime.sum(startTime, recipe.time).compareTo(Game.world.time()) == 0)
      Some(new ResourcePack(recipe.output._1, recipe.output._2))

    else None
  }

}
