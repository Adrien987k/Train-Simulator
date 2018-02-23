package interface

import engine.Town
import link.Change
import utils.Pos

import scala.collection.mutable.ArrayBuffer

object GUI {

  //Ad
  def init(): Unit = {
    //Create feunêtre canvas boutons
  }

  //Eng
  def initWorld(towns: ArrayBuffer[Town]): Unit = {
    //
  }

  //Eng
  def display(changes: ArrayBuffer[Change]): Unit = {
    for (change <- changes) {
      //Apply change
    }
    //Effacer les trains
  }

  //Ad
  def displayTrain(pos : Pos): Unit = {

  }

}
