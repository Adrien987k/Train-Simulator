package logic.economy

import logic.economy.Resources._

object Consumption {

  def initialConsumption() : ResourceMap = {
    val consumption = new ResourceMap()

    consumption.addSome(ALUMINIUM, 2000)
    consumption.addSome(BAKED_GOOD, 1500)
    consumption.addSome(GRAIN, 2000)
    consumption.addSome(TEXTILE, 250)

    consumption
  }

}
