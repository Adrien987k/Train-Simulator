package interface

import engine.{ItemType, World}

import scala.collection.mutable.ListBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, ButtonBar}
import scalafx.scene.layout.Border

object ItemsButtonBar {

  var selected:Option[ItemType.Value] = None

  def select(itemType: ItemType.Value = null): Unit = {
    selected = Option(itemType)
  }

  def makeItemsButtonsBar(): ButtonBar = {
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
