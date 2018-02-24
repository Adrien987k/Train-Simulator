package interface

import engine.ItemType

import scalafx.scene.control.{Button, ButtonBar}
import scalafx.scene.layout.Border

class ItemsButtonBar extends ButtonBar {

  init()

  def init() : Unit = {
    var itemButtons : List[Button] = List.empty

    for (item <- ItemType.values)
      itemButtons = new Button(item.toString) :: itemButtons

    this.buttons = itemButtons
  }

}
