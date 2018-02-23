package engine

import interface.GUI
import utils.Pos

class BasicTrain(pos : Pos) extends Train(pos : Pos) {

  override def place(): Unit = {

  }

  override def render(): Unit = {
    GUI.displayTrain(pos)
  }

  override def step(): Unit = {
    //
    render()
  }

  override def info(): String = ???

  override def getPos(): Pos = pos
}
