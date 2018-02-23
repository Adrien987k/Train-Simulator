package engine

import utils.Pos

trait Updatable {

  def getPos(): Pos

  def step()

  def info(): String
}
