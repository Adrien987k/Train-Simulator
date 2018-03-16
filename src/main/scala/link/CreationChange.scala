package link

import logic.items.ItemTypes.ItemType
import utils.Pos

class CreationChange(_pos1: Pos, _pos2: Pos, _itemType: ItemType) extends Change {

  def pos1: Pos = _pos1
  def pos2: Pos = _pos2
  def itemType: ItemType = _itemType

  def this(pos: Pos, itemType: ItemType) = {
    this(pos, null, itemType)
  }

}
