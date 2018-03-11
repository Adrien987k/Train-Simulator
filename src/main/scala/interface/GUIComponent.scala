package interface

import scalafx.scene.Node

trait GUIComponent {

  def make(): Node

  def restart()

}
