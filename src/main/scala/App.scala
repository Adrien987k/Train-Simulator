
import interface.GUI

import scalafx.application.JFXApp

object App extends JFXApp {

  stage = GUI.makePrimaryStage()

  val game = new Game

}
