package logic.items.production

import game.Game
import logic.economy.ResourcePack
import utils.DateTime

class Production(val recipe : Recipe) {

  var startTime : DateTime = _

  def start(input : List[ResourcePack]) : Unit = {
    //TODO Check input = recipe.input

    startTime = Game.world.time()
  }

  def start() : Unit = {
    if (recipe.input.nonEmpty) return

    startTime = Game.world.time()
  }

  /**
    * @return A Some(pack) if one is produced, None otherwise
    */
  def checkIsFinished : Option[ResourcePack] = {
    if (DateTime.sum(startTime, recipe.time).compareTo(Game.world.time()) == 0)
      Some(new ResourcePack(recipe.output._1, recipe.output._2))

    else None
  }

}
