package interface
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

object OneTrainInformationPanel extends GUIComponent {


  val defaultPanel : Node = new Label("No train selected")
  var panel : BorderPane = new BorderPane

  override def make(): Node = {
    panel.center = defaultPanel
    panel
  }

  def addPanel(newPanel : Node): Unit = {
    panel.center = newPanel
  }

  def removePanel(): Unit = {
    panel.center = defaultPanel
  }

}
