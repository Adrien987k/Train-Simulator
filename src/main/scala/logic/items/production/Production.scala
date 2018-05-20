package logic.items.production

import game.Game
import logic.economy.ResourcePack
import utils.DateTime

class Production(val recipe : Recipe) {

  var startTime : DateTime = _

  def start(input : List[ResourcePack]) : Unit = {
    //TODO Check input = recipe.input

    startTime = Game.world.gameDateTime.copy()
  }

  def start() : Unit = {
    if (recipe.input.nonEmpty) return

    startTime = Game.world.gameDateTime.copy()
  }

  def checkIsFinished : Option[ResourcePack] = {
    /*println("START TIME : " + startTime)
    println("RECIPE TIME : " + recipe.time)
    println("SUM : " + DateTime.sum(startTime, recipe.time))
    println("WORLD TIME : " + Game.world.gameDateTime + "\n")*/

    if (DateTime.sum(startTime, recipe.time).compareTo(Game.world.gameDateTime) == 0)
      Some(new ResourcePack(recipe.output._1, recipe.output._2))

    else None
  }

}
