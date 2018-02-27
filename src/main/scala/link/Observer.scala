package link

import scala.collection.mutable.ListBuffer

trait Observer {

  def notifyChange(changes : ListBuffer[Change])

  def register(observable : Observable) : Unit = {
    observable.observers += this
  }

  def unregister(observable : Observable) : Unit = {
    observable.observers -= this
  }

}
