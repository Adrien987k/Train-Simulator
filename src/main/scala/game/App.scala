package game

import interface.GUI

import scalafx.application.JFXApp

object App extends JFXApp {

  Game.makeWorld()

  stage = GUI.makePrimaryStage()

  Game.start()


}
