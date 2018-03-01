package engine

import utils.Pos

import scalafx.scene.Node

trait Updatable {

  def pos : Pos

  def step()

  def propertyPane(): Node
}
