package logic.items

import logic.PointUpdatable
import logic.items.ItemTypes.FacilityType
import logic.world.Company
import logic.world.towns.Town

abstract class Facility
(val facilityType : FacilityType,
 override val company : Company,
 val town : Town)
  extends Item(facilityType, company) with PointUpdatable {

  updateRate(1)
  pos = town.pos

}
