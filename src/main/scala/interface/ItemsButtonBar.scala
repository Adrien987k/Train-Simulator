package interface

import logic.items.ItemTypes
import logic.items.ItemTypes.ItemType
import logic.world.Shop

import scala.collection.mutable.ListBuffer
import scalafx.scene.control.{Button, ButtonBar}
import scalafx.scene.text.{Font, FontWeight}

object ItemsButtonBar extends GUIComponent {

  val bar : ButtonBar = new ButtonBar
  var itemButtons : ListBuffer[Button] = ListBuffer.empty

  var selected : Option[ItemType] = None

  var buildMode = false

  def select(itemType: ItemType = null) : Unit = {
    selected = Option(itemType)
  }

  override def make() : ButtonBar = {
    val buildModeButton = new Button("Game mode")

    buildModeButton.font = Font.font(null, FontWeight.Bold, 18)
    buildModeButton.setStyle("-fx-background-color: lightCoral")
    buildModeButton.onAction = _ => {
      if (!buildMode) {
        selected = None
        buildMode = true
        bar.style = "-fx-background-color: black"
        buildModeButton.text = "Build mode"
      } else {
        println("default")
        buildMode = false
        bar.style = "-fx-background-color: white"
        buildModeButton.text = "Game mode"
      }
    }
    itemButtons += buildModeButton

    for (item <- ItemTypes.onSaleItems()) {
      val itemButton = new Button(item.name + " " + Shop.price(item) + "$" +
        (item match {case ItemTypes.RAIL => " per KM" case _ => ""}))
      itemButton.style = buildModeButton.style()
      itemButton.onAction = _ => {
        if (buildMode) {
          select(item)
          itemButtons.foreach(button =>
            if (button != buildModeButton) button.style = buildModeButton.style()
          )
          itemButton.style = "-fx-background-color: darkGrey"
        }
      }
      itemButtons += itemButton
    }

    bar.buttons = itemButtons
    bar
  }

  override def restart() : Unit = {
    select()
  }

  override def update() : Unit = {

  }

}
