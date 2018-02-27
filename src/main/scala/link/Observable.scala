package link

import scala.collection.mutable.ListBuffer

trait Observable {

  var changes : ListBuffer[Change] = ListBuffer.empty
  var observers : ListBuffer[Observer] = ListBuffer.empty

  def addChange(change : Change) : Unit = {
    changes += change
  }

  def notifyObservers() : Unit = {
    observers.foreach(_.notifyChange(changes))
    changes = ListBuffer.empty
  }

}
