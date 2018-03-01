package interface
import scalafx.scene.Node
import scalafx.scene.layout.VBox

object OneTrainInformationPanel extends GUIComponent {

  val panel = new VBox()

  override def make(): Node = {
    panel
  }

}
