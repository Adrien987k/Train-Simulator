package logic

import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.text.{Font, FontWeight}

trait Updatable {

  private var counter = 0
  private var _updateRate = 0

  var labels : List[Label] = List.empty

  def updateRate() : Int = _updateRate
  def updateRate(value : Int) : Unit = _updateRate = value

  def step() : Boolean = {
    counter += 1
    if (counter < _updateRate) {
      return false
    }
    counter = 0
    true
  }

  def propertyPane() : Node

  def styleLabels(size : Int = 18) : Unit = {
    labels.foreach(_.font = Font.font(null, FontWeight.Bold, size))
  }
}
