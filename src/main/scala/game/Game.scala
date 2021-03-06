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

  def save () : Unit = {
    scala.xml.XML.save("save.xml", _world.get.save)
  }

  def load() : Unit = {
    GUI.restart()
    val node = scala.xml.XML.loadFile("save.xml")
    _world = Some (new World)
    _world.get.load(node)
  }

}
