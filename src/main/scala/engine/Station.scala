package engine

import scala.collection.mutable.ArrayBuffer

abstract class Station() extends Item with Updatable {

  val DEFAULT_CAPACITY = 5

  val name = ""
  var capacity = DEFAULT_CAPACITY

  val train:ArrayBuffer[Train] = ArrayBuffer.empty
  val rails:ArrayBuffer[Rail] = ArrayBuffer.empty

}
