package logic.economy

import scala.util.Random

object Consumption {

  private val rand = new Random

  def initialConsumption() : ResourceMap = {
    val consumption = new ResourceMap()

    val resources = Resources.resources()

    val randResourceConsume = rand.nextInt(5)

    for(i <- 0 to randResourceConsume) {
      val randResource = rand.nextInt(resources.size - 1)
      val randQuantity = rand.nextInt(200)

      consumption.addSome(resources(randResource), randQuantity)
    }

    consumption
  }

}
