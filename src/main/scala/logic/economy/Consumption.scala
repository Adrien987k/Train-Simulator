package logic.economy

import logic.economy.Resources._

object Consumption {

  def initialConsumption() : Consumption = {
    val consumption = new Consumption

    consumption.addSome(ALUMINIUM, 50)
    consumption.addSome(BAKED_GOOD, 100)
    consumption.addSome(GRAIN, 200)
    consumption.addSome(TEXTILE, 25)

    consumption
  }

}

class Consumption extends ResourceMap("Consumption per day") {

}
