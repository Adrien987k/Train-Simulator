package interface

import logic.items.ItemTypes
import logic.items.ItemTypes.ItemType
import logic.world.Shop

import scala.collection.mutable.ListBuffer
import scalafx.scene.control.{Button, ButtonBar}

object ItemsButtonBar extends GUIComponent {

  var selected:Option[ItemType] = None

  def select(itemType: ItemType = null): Unit = {
    selected = Option(itemType)
  }

  def make(): ButtonBar = {
    val bar : ButtonBar = new ButtonBar
    var itemButtons : ListBuffer[Button] = ListBuffer.empty

    for (item <- ItemTypes.items()) {
      val itemButton = new Button(item.name + " " + Shop.price(item) + "$" +
        (item match {case ItemTypes.RAIL => " per KM" case _ => ""}))
      itemButton.onAction = _ => {
        select(item)
      }
      itemButtons += itemButton
    }

    bar.buttons = itemButtons
    bar
  }

  override def restart(): Unit = {
    select()
  }

}
