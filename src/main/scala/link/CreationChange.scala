package link

import logic.items.ItemTypes
import utils.Pos

class CreationChange(_pos1: Pos, _pos2: Pos, _itemType: ItemTypes.Value) extends Change {

  def pos1: Pos = _pos1
  def pos2: Pos = _pos2
  def itemType: ItemTypes.Value = _itemType

  def this(pos: Pos, itemType: ItemTypes.Value) = {
    this(pos, null, itemType)
  }

}
