package logic

import scalafx.scene.Node

trait Updatable {

  private var counter = 0
  private var _updateRate = 0

  def updateRate() : Int = _updateRate
  def updateRate(value : Int) : Unit = _updateRate = value

  def step() : Unit = {
    counter += 1
    if (counter < _updateRate) {
      return
    }
    counter = 0
  }

  def propertyPane(): Node
}
