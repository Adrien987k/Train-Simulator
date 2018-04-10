package logic.economy

import logic.economy.Resources._

object Consumption {

  def initialConsumption() : Consumption = {
    val consumption = new Consumption

    consumption.addSome(VEGETABLE, 200)
    consumption.addSome(FRUIT, 100)
    consumption.addSome(FUEL, 50)
    consumption.addSome(COTTON, 25)

    consumption
  }

}

class Consumption extends ResourceMap("Consumption per day") {

}
