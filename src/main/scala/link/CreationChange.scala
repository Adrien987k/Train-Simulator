package link

import engine.ItemType.ItemType
import utils.Pos

class CreationChange(pos1: Pos, pos2: Pos, itemType: ItemType) extends Change {

  def this(pos: Pos, itemType: ItemType) = {
    this(pos, null, itemType)
  }

}
