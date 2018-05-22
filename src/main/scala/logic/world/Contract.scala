package logic.world

import logic.economy.ResourcePack
import logic.world.towns.Town
import statistics.StatValue

import scala.collection.mutable.ListBuffer

sealed class Status

case object CREATED extends Status
case object VEHICLE_SENT extends Status
case object FULFILLED extends Status

case class Contract
(from : Town,
 to : Town,
 packs : ListBuffer[ResourcePack],
 reward : Double,
 var status : Status)
  extends StatValue {
  override def info() : String = {
    val sb = packs.foldLeft(new StringBuilder)((sb, pack) => {
      sb.append("\n" + pack.info())
    })

    val statusStr = status match {
      case CREATED => " Status: created"
      case VEHICLE_SENT => " Status: Vehicle sent"
      case FULFILLED => " Status: fulfilled"
      case _ => ""
    }

    "from: " + from.name + " | to: " + to.name +
      " reward: $" + reward.toInt +
      statusStr +
      sb.toString()
  }

  override def average(l : ListBuffer[StatValue]) : StatValue = l.head

  override def sum(v : StatValue): StatValue = v
}
