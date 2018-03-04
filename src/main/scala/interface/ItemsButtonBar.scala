package interface

import engine.ItemType

import scala.collection.mutable.ListBuffer
import scalafx.scene.control.{Button, ButtonBar}

object ItemsButtonBar extends GUIComponent {

  var selected:Option[ItemType.Value] = None

  def select(itemType: ItemType.Value = null): Unit = {
    selected = Option(itemType)
  }

  def make(): ButtonBar = {
    val bar : ButtonBar = new ButtonBar
    var itemButtons : ListBuffer[Button] = ListBuffer.empty

    for (item <- ItemType.values) {
      val itemButton = new Button(item.toString)
      itemButton.onAction = _ => {
        select(item)
      }
      itemButtons += itemButton
    }

    bar.buttons = itemButtons
    bar
  }

}
