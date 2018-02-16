package engine

import engine.ItemType.{ItemType, ROAD, STATION, TRAIN}

object ItemFactory {

  def buildItem(item: ItemType): Item = {
    item match {
      case TRAIN => new BasicTrain
      case STATION => new BasicStation
      case ROAD => new BasicRail
    }
  }

}
