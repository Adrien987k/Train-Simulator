package logic.items

import scala.collection.mutable.ListBuffer

class EvolutionPlan
(val planLists : List[List[Double]],
 val basePrice : Double,
 val pricePerLevel : Double) {

  def isMaxLevel(lev : Int) : Boolean = {

    val max = planLists.foldLeft(0)((max, plan) => {
      math.max(max, plan.size)
    })

    lev >= max
  }

  def level(lev : Int) : List[Double] = {
    val result = ListBuffer.empty[Double]

    planLists.foreach(list => {
      if (list.isDefinedAt(lev - 1)) {
        result += list(lev - 1)
      } else {
        result += list.last
      }
    })

    result.toList
  }

}
