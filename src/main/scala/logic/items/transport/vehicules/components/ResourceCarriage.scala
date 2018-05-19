package logic.items.transport.vehicules.components

import logic.economy.{Cargo, ResourceMap}
import logic.economy.Resources.ResourceType
import logic.items.transport.vehicules.components.TrainComponentTypes.RESOURCE_CARRIAGE
import logic.world.Company

import scala.collection.mutable.ListBuffer

class ResourceCarriage
(val resourceType : ResourceType,
 override val company : Company,
 override val evolutionPlan : CarriageEvolutionPlan)
  extends Carriage(RESOURCE_CARRIAGE, company, evolutionPlan) {

  private var cargoCapacity : Double = evolutionPlan.level(level)(2)

  def weightCapacity : Double = cargoCapacity

  var cargos : ListBuffer[Cargo] = ListBuffer.empty

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

  /*
  def addResourcePack(resourcePack : ResourcePack) : Unit = {
    if (resourcePack.resource.resourceType != resourceType) return

    val resourcesWeight = resources.foldLeft(0.0)((total, resPack) => total + resPack.weight)
    if (resourcePack.weight + resourcesWeight > _weight) {

    }

    resources += resourcePack
  }

  def takeResources() : ListBuffer[ResourcePack] = {
    val resourcesResult = resources

    resources = ListBuffer.empty

    resourcesResult
  }
  */

  def resourceMap() : ResourceMap = {
    val result = new ResourceMap()

    cargos.foldLeft(result)((map, cargo) => {
      cargo.resourceMap().merge(map)
    })
  }

}
