package logic.items.transport.vehicules.components

import logic.Loadable
import logic.economy.{Cargo, ResourceMap}
import logic.economy.Resources.ResourceType
import logic.items.transport.vehicules.components.TrainComponentTypes.RESOURCE_CARRIAGE
import logic.world.Company

import scala.collection.mutable.ListBuffer
import scala.xml.Node

class ResourceCarriage
(val resourceType : ResourceType,
 override val company : Company,
 override val evolutionPlan : CarriageEvolutionPlan)
  extends Carriage(RESOURCE_CARRIAGE, company, evolutionPlan) with Loadable{

  private var cargoCapacity : Double = evolutionPlan.level(level)(2)

  def weightCapacity : Double = cargoCapacity

  var cargos : ListBuffer[Cargo] = ListBuffer.empty

  override def load(node: Node): Unit = {

  }

  override def save: Node = {
    <ResourceCarriage level={level.toString}>
      {cargos.foldLeft(scala.xml.NodeSeq.Empty)((acc, cargo) => acc++cargo.save)}
    </ResourceCarriage>
  }

  def loadCargo(cargo : Cargo) : Unit = {
    if (cargo.resourceType.compareTo(resourceType) == 0)
      cargos += cargo
  }

  def loadCargos(newCargos : ListBuffer[Cargo]) : Unit = {

    cargos ++= newCargos
  }

  def unloadCargos() : ListBuffer[Cargo] = {
    val result = cargos

    cargos = ListBuffer.empty

    result
  }

  override def evolve(): Unit = {
    super.evolve()

    cargoCapacity = evolutionPlan.level(level)(2).toInt

    company.buy(evolvePrice)
  }

  def resourceMap() : ResourceMap = {
    val result = new ResourceMap()

    cargos.foldLeft(result)((map, cargo) => {
      cargo.resourceMap().merge(map)
    })
  }

}
