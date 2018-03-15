package logic.world

import logic.Updatable
import utils.Pos

trait LineUpdatable extends Updatable {

  private var _posA = new Pos(0, 0)
  private var _posB = new Pos(0, 0)

  def posA : Pos = _posA
  def posA_= (value : Pos) : Unit = _posA = value
  def posA_= (x : Double, y : Double) : Unit = _posA = new Pos(x, y)

  def posB : Pos = _posB
  def posB_= (value : Pos) : Unit = _posB = value
  def posB_= (x : Double, y : Double) : Unit = _posB = new Pos(x, y)

}
