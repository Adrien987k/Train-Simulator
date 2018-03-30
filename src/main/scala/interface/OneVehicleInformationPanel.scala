package interface
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

object OneVehicleInformationPanel extends GUIComponent {


  val defaultPanel : Node = new Label("No vehicle selected")
  var panel : BorderPane = new BorderPane

  override def make(): Node = {
    panel.center = defaultPanel
    panel.style = "-fx-background-color: lightGreen"
    panel
  }

  override def restart(): Unit = {
    panel.center = defaultPanel
  }

  override def update() : Unit = {

  }

  def addPanel(newPanel : Node): Unit = {
    panel.center = newPanel
  }

  def removePanel(): Unit = {
    panel.center = defaultPanel
  }

}
