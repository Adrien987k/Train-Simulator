package interface

import logic.items.ItemTypes.ItemType
import logic.items.{ItemTypes, VehicleCategories}
import logic.items.VehicleCategories.VehicleCategory
import logic.items.production.FactoryTypes
import logic.items.transport.roads.RoadTypes.{HIGHWAY, RAIL}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout.BorderPane
import scalafx.scene.text.{Font, FontWeight}

object ItemsButtonBar extends GUIComponent {

  private val mainPane = new BorderPane

  private val bar : ButtonBar = new ButtonBar
  private var itemChoiceButtons : ListBuffer[Button] = ListBuffer.empty

  private val trainCategoryButton = new Button("Trains")
  private val planesCategoryButton = new Button("Planes")
  private val shipsCategoryButton = new Button("Ships")
  private val trucksCategoryButton = new Button("Trucks")
  private val factoriesButton = new Button("Factories")

  private var buttonsForItemCategory : mutable.HashMap[VehicleCategory, List[Button]] = mutable.HashMap.empty

  private var factoriesButtons : List[Button] = List.empty

  private var selected : Option[(ItemType, Option[Button])] = None

  private var _buildMode = false

  private val buildModeButton = new Button("Game mode")

  def buildMode : Boolean = _buildMode

  def selectedItem : Option[ItemType] = selected match {
    case Some(itemAndButton) => Some(itemAndButton._1)
    case None => None
  }

  def select(itemAndButton : (ItemType, Option[Button]) = null) : Unit = {
    selected.foreach(_._2.foreach(_.style = "-fx-background-color: lightCoral"))

    if (itemAndButton != null) {
      itemAndButton._2.foreach(_.style = "-fx-background-color: darkGrey")
      selected = Option(itemAndButton._1, itemAndButton._2)
    }
  }

  def buildButtonForCategory(vehicleCategory : VehicleCategory) : Unit = {
    val buttons = ListBuffer.empty[Button]

    ItemTypes.onSaleItemsForVehicleCategory(vehicleCategory).foldLeft(buttons)(
      (buttons, itemType) => {

        val itemButton = new Button(itemType.name + itemType.price + "$" +
          (itemType match {case RAIL | HIGHWAY => " per KM" case _ => ""}))

        itemButton.style = buildModeButton.style()

        itemButton.onAction = _ => {
          if (_buildMode) {
            select(itemType, Some(itemButton))
          }
        }

        buttons += itemButton
      })

    buttonsForItemCategory.+=((vehicleCategory, buttons.toList))
  }

  def buildButtonForFactories() : Unit = {
    val buttons = ListBuffer.empty[Button]

    FactoryTypes.factories().foldLeft(buttons)(
      (buttons, item) => {

        val itemButton = new Button(item.name)
        itemButton.style = buildModeButton.style()

        itemButton.onAction = _ => {
          if (_buildMode) {
            select(item, Some(itemButton))
          }
        }

        buttons += itemButton
      })

    factoriesButtons = buttons.toList
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
      select()

      if (!_buildMode) {
        _buildMode = true
        bar.style = "-fx-background-color: black"
        buildModeButton.text = "Build mode"
      } else {
        _buildMode = false
        bar.style = "-fx-background-color: white"
        buildModeButton.text = "Game mode"
      }
    }
    itemChoiceButtons += buildModeButton

    buildButtonForCategory(VehicleCategories.Trains)
    buildButtonForCategory(VehicleCategories.Planes)
    buildButtonForCategory(VehicleCategories.Ships)
    buildButtonForCategory(VehicleCategories.Trucks)

    buildButtonForFactories()

    addActionToCategoryButton(trainCategoryButton, VehicleCategories.Trains)
    addActionToCategoryButton(planesCategoryButton, VehicleCategories.Planes)
    addActionToCategoryButton(shipsCategoryButton, VehicleCategories.Ships)
    addActionToCategoryButton(trucksCategoryButton, VehicleCategories.Trucks)

    factoriesButton.onAction = _ => {
      val factoryTypes = FactoryTypes.factories()

      val factoryButtons = factoryTypes.foldLeft(ListBuffer[Button]())((buttonTypes, ftype) => {
        buttonTypes += new Button(ftype.name + " $" + ftype.price)
      })

      val choices =
        factoryButtons.foldLeft(ListBuffer[String]())((choices, button) => {
          choices += button.text()
        })

      case class FactoryResult(name : String)

      val dialog = new ChoiceDialog(defaultChoice = choices.head, choices) {
        initOwner(GUI.stage)
        title = "Factories"
        headerText = "Choose a factory to build"
      }

      dialog.showAndWait() match {
        case Some(text) =>
          println(text)
          factoryTypes.find(ftype => text.contains(ftype.name)) match {
            case Some(ftype) => select(ftype, None)
            case None =>
          }

        case _ => println("... user chose CANCEL or closed the dialog")
      }
    }

    itemChoiceButtons += factoriesButton

    bar.buttons = itemChoiceButtons

    mainPane.left = bar

    mainPane
  }

  override def restart() : Unit = {
    select()

    _buildMode = false
    bar.style = "-fx-background-color: white"
    buildModeButton.text = "Game mode"
  }

  override def update() : Unit = {

  }

}
