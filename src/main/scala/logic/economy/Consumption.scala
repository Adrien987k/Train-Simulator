package logic.economy

import logic.economy.Resources._

object Consumption {

  def initialConsumption() : ResourceMap = {
    val consumption = new ResourceMap()

    consumption.addSome(ALUMINIUM, 200)
    consumption.addSome(BAKED_GOOD, 150)
    consumption.addSome(TEXTILE, 25)

    consumption
  }

}
