package engine

import utils.Pos

trait Updatable {

  var pos : Pos = new Pos(0, 0)

  def step()

  def info(): String
}
