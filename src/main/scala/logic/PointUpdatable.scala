package logic

import utils.Pos

trait PointUpdatable extends Updatable {

  var _pos = new Pos(0, 0)

  def pos : Pos = _pos

  def pos_= (value : Pos) : Unit = _pos = value
  def pos_=(x : Double, y : Double) : Unit = { _pos = new Pos(x, y) }

}
