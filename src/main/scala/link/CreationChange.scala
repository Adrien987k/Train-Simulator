package link

import engine.ItemType
import utils.Pos

class CreationChange(pos1: Pos, pos2: Pos, itemType: ItemType.Value) extends Change {

  def this(pos: Pos, itemType: ItemType.Value) = {
    this(pos, null, itemType)
  }

}
