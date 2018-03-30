package logic.items

import logic.items.ItemTypes.ItemType
import logic.world.Company

abstract class Item(val itemType : ItemType, val company: Company) {

}
