package link

import engine.ItemType
import utils.Pos

class CreationChange(_pos1: Pos, _pos2: Pos, _itemType: ItemType.Value) extends Change {

  def pos1: Pos = _pos1
  def pos2: Pos = _pos2
  def itemType: ItemType.Value = _itemType

  def this(pos: Pos, itemType: ItemType.Value) = {
    this(pos, null, itemType)
  }

}
