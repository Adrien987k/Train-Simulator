package interface

import logic.items.{ItemTypes, VehicleCategories}
import logic.items.ItemTypes.ItemType
import logic.items.VehicleCategories.VehicleCategory
import logic.world.Shop

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control.{Button, ButtonBar}
import scalafx.scene.layout.BorderPane
import scalafx.scene.text.{Font, FontWeight}

object ItemsButtonBar extends GUIComponent {
  val mainPane = new BorderPane

  val bar : ButtonBar = new ButtonBar
  var itemChoiceButtons : ListBuffer[Button] = ListBuffer.empty

  val trainCategoryButton = new Button("Trains")
  val planesCategoryButton = new Button("Planes")
  val shipsCategoryButton = new Button("Ships")
  val trucksCategoryButton = new Button("Trucks")

  var buttonsForItemCategory : mutable.HashMap[VehicleCategory, List[Button]] = mutable.HashMap.empty

  var selected : Option[(ItemType, Button)] = None

  var buildMode = false

  val buildModeButton = new Button("Game mode")

  def selectedItem : Option[ItemType] = selected match {
    case Some(itemAndButton) => Some(itemAndButton._1)
    case None => None
  }

  def select(itemAndButton : (ItemType, Button) = null) : Unit = {
    selected.foreach(_._2.style = "-fx-background-color: lightCoral")

    if (itemAndButton != null) {
      itemAndButton._2.style = "-fx-background-color: darkGrey"
      selected = Option(itemAndButton._1, itemAndButton._2)
    }
  }

  def buildButtonForCategory(vehicleCategory : VehicleCategory) : Unit = {
    val buttons = ListBuffer.empty[Button]

    ItemTypes.onSaleItemsForVehicleCategory(vehicleCategory).foldLeft(buttons)(
      (buttons, item) => {

        val itemButton = new Button(item.name + " " + Shop.price(item) + "$" +
          (item match {case ItemTypes.RAIL | ItemTypes.HIGHWAY => " per KM" case _ => ""}))

        itemButton.style = buildModeButton.style()

        itemButton.onAction = _ => {
          if (buildMode) {
            select(item, itemButton)
          }
        }

        buttons += itemButton
      })

    buttonsForItemCategory.+=((vehicleCategory, buttons.toList))
  }

  def addActionToCategoryButton(button : Button, category : VehicleCategory) : Unit = {
    button.onAction = _ => {
      val leftBar = new ButtonBar
      leftBar.buttons = buttonsForItemCategory(category)

      mainPane.right = leftBar
    }

    itemChoiceButtons += button
  }

  override def make() : Node = {
    buildModeButton.font = Font.font(null, FontWeight.Bold, 18)
    buildModeButton.style = "-fx-background-color: lightCoral"
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
    itemChoiceButtons += buildModeButton

    buildButtonForCategory(VehicleCategories.Trains)
    buildButtonForCategory(VehicleCategories.Planes)
    buildButtonForCategory(VehicleCategories.Ships)
    buildButtonForCategory(VehicleCategories.Trucks)

    addActionToCategoryButton(trainCategoryButton, VehicleCategories.Trains)
    addActionToCategoryButton(planesCategoryButton, VehicleCategories.Planes)
    addActionToCategoryButton(shipsCategoryButton, VehicleCategories.Ships)
    addActionToCategoryButton(trucksCategoryButton, VehicleCategories.Trucks)

    bar.buttons = itemChoiceButtons

    mainPane.left = bar

    mainPane
  }

  override def restart() : Unit = {
    select()

    buildMode = false
    bar.style = "-fx-background-color: white"
    buildModeButton.text = "Game mode"
  }

  override def update() : Unit = {

  }

}
