package interface

import logic.items.ItemTypes
import logic.world.Shop

import scala.collection.mutable.ListBuffer
import scalafx.scene.control.{Button, ButtonBar}

object ItemsButtonBar extends GUIComponent {

  var selected:Option[ItemTypes.Value] = None

  def select(itemType: ItemTypes.Value = null): Unit = {
    selected = Option(itemType)
  }

  def make(): ButtonBar = {
    val bar : ButtonBar = new ButtonBar
    var itemButtons : ListBuffer[Button] = ListBuffer.empty

    for (item <- ItemTypes.values) {
      val itemButton = new Button(item.toString + " " + Shop.price(item) + "$" +
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
