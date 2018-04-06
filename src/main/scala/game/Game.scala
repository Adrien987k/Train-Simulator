package game

import interface.GUI
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

  def newGame() : Unit = {
    GUI.restart()

    _world = Some(new World)
    _world.get.start()
  }

}
