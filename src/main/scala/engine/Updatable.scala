package engine

import utils.Pos

trait Updatable {

  def pos : Pos

  def step()

  def info(): String
}
