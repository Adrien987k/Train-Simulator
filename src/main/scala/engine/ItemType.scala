package engine

object ItemType {

  sealed abstract class ItemType {

  }

  case object TRAIN   extends ItemType
  case object TOWN    extends ItemType
  case object STATION extends ItemType
  case object ROAD    extends ItemType
}
