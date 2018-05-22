package logic.economy

import logic.Loadable
import logic.economy.Resources.{Resource, ResourceType}

import scala.collection.mutable.ListBuffer
import scala.xml.Node

class Cargo
(val resourceType : ResourceType) extends  Loadable {

  val resources : ResourceCollection = new ResourceCollection()

  override def load(node: Node): Unit = {

  }

  override def save: Node = {
    <Cargo>
      {resources.packs.foldLeft(scala.xml.NodeSeq.Empty)((acc, resourcePack) => acc++resourcePack.save)}
    </Cargo>
  }

  def totalWeight : Double = {
    resources.totalWeight
  }

  def store(packs : ListBuffer[ResourcePack]) : Unit = {
    val packsToStore = packs.filter(pack => {
      pack.resource.resourceType.compareTo(resourceType) == 0
    })

    resources.storeResourcePacks(packsToStore)
  }

  def takeAll() : List[ResourcePack] = {
    resources.takeAll()
  }

  def resourceMap() : ResourceMap = resources.resourceMap()

}
