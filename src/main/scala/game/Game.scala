package game

import logic.world.World

object Game {

  private var _world : Option[World] = None

  def world : World = _world.get

  def makeWorld() : Unit = {
    _world = Some(new World)
  }

  def start() : Unit = {
    world.start()
  }

}
