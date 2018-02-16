package interface

import link.Change

import scala.collection.mutable.ArrayBuffer

object GUI {

  def display(changes: ArrayBuffer[Change]): Unit = {
    for (change <- changes) {
      //Apply change
    }
  }

}
