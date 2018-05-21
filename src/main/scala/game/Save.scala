package game

import game.Game.world
import logic.world.towns.Town
import logic.economy.{Cargo, ResourcePack, Resources}
import logic.items.production.{Factory, Production}
import logic.items.transport.roads.Road
import logic.items.transport.vehicules.components.{Carriage, PassengerCarriage, ResourceCarriage}
import logic.items.transport.vehicules._
import logic.world.World
import statistics.{DoubleValue, IntValue, NoValue, StringValue}

import scala.xml.NodeSeq


object Save {

  def createNodeResourcePack (resourcePack : ResourcePack) : scala.xml.Node = {
      <Resource
      type={resourcePack.resource.name.toString}
      quantity={resourcePack.quantity.toString}
      />
  }

  def createNodeRequest (resourcePack : (Resources.Resource, Int)) : scala.xml.Node = {
      <Resource
      type={resourcePack._1.name.toString}
      quantity={resourcePack._2.toString}
      />
  }

  def createNodeConsumption(resourcePack : (Resources.Resource, Int)) : scala.xml.Node = {
      <Resource
      type={resourcePack._1.name.toString}
      quantity={resourcePack._2.toString}
      />
  }

  def createNodeProduction (factory: Factory, production: Production) : scala.xml.Node = {
      <Production
      recipe={factory.recipes.indexOf(production.recipe).toString}
      startday={production.startTime.days.toString}
      starthour={production.startTime.hours.toString}
      />
  }

  def createNodeFactory (factory: Factory) : scala.xml.Node = {
    <Factory type={factory.factoryType.name.toString}>
      {factory.productions.foldLeft(scala.xml.NodeSeq.Empty)((acc, production) =>
        acc++createNodeProduction(factory, production))}
    </Factory>
  }

  def createNodeEvent(event : statistics.Statistics#Event) : scala.xml.Node = {
    <Event
      day={event.time.days.toString}
      hour={event.time.hours.toString}
      name={event.name}
      type={event.value match {
        case IntValue(v) => "Int"
        case DoubleValue(v) => "Double"
        case StringValue(v) => "String"
        case NoValue() => "No"
      }
      }
      value={event.value.info()}
      />
  }

  def createNodeResourceCargo(cargo : Cargo) : scala.xml.Node = {
    <Cargo>
      {cargo.resources.packs.foldLeft(scala.xml.NodeSeq.Empty)((acc, resourcePack) => acc++createNodeResourcePack(resourcePack))}
    </Cargo>
  }

  def createNodeResourceCarriage(carriage: ResourceCarriage) : scala.xml.Node = {
    <ResourceCarriage level={carriage.level.toString}>
      {carriage.cargos.foldLeft(scala.xml.NodeSeq.Empty)((acc, cargo) => acc++createNodeResourceCargo(cargo))}
    </ResourceCarriage>
  }

  def createNodePassengerCarriage(carriage: PassengerCarriage) : scala.xml.Node = {
    <PassengerCarriage level={carriage.level.toString} passenger={carriage.nbPassenger.toString}/>
  }

  def createNodeCarriage(carriage: Carriage) : scala.xml.Node = {
    carriage match {
      case carriage : ResourceCarriage => createNodeResourceCarriage(carriage)
      case carriage : PassengerCarriage => createNodePassengerCarriage(carriage)
    }
  }

  def createNodeDieselTrain (train: Train) : scala.xml.Node = {
    <DieselTrain
    level={train.level.toString}
    fuel={train.engine.fuelLevel.toString}
    >
      {train.carriages.foldLeft(scala.xml.NodeSeq.Empty)((acc, carriage) => acc++createNodeCarriage(carriage))}
      {createNodeVehicleLocation(train)}
      {createNodeVehicleGoal(train)}
      {createNodeVehicleDestination(train)}
    </DieselTrain>
  }

  def createNodeElectricTrain(train: Train) : scala.xml.Node = {
      <ElectricTrain
      level={train.level.toString}
      fuel={train.engine.fuelLevel.toString}
      >
        {train.carriages.foldLeft(scala.xml.NodeSeq.Empty)((acc, carriage) => acc++createNodeCarriage(carriage))}
        {createNodeVehicleLocation(train)}
        {createNodeVehicleGoal(train)}
        {createNodeVehicleDestination(train)}
      </ElectricTrain>
  }

  def createNodeBoeing(plane: Plane) : scala.xml.Node = {
      <Boeing
      level={plane.level.toString}
      fuel={plane.fuelLevel.toString}
      passenger={plane.nbPassenger().toString}
      >
        {createNodeVehicleLocation(plane)}
        {createNodeVehicleGoal(plane)}
        {createNodeVehicleDestination(plane)}
      </Boeing>
  }

  def createNodeConcorde(plane: Plane) : scala.xml.Node = {
    <Concorde
    level={plane.level.toString}
    fuel={plane.fuelLevel.toString}
    passenger={plane.nbPassenger().toString}
    >
      {createNodeVehicleLocation(plane)}
      {createNodeVehicleGoal(plane)}
      {createNodeVehicleDestination(plane)}
    </Concorde>
  }

  def createNodeTruck(truck: Truck) : scala.xml.Node = {
    <Truck
    level={truck.level.toString}
    fuel={truck.fuelLevel.toString}
    >
      {truck.cargoOpt match {
        case Some(cargo) => createNodeResourceCargo(cargo)
        case _ => ()
        }
      }
      {createNodeVehicleLocation(truck)}
      {createNodeVehicleGoal(truck)}
      {createNodeVehicleDestination(truck)}
    </Truck>
  }

  def createNodeLiner(ship: Ship) : scala.xml.Node = {
    <Liner
    level={ship.level.toString}
    fuel={ship.fuelLevel.toString}
    >
      {ship.cargoes.foldLeft(scala.xml.NodeSeq.Empty)((acc, cargo) => acc++createNodeResourceCargo(cargo))}
      {createNodeVehicleLocation(ship)}
      {createNodeVehicleGoal(ship)}
      {createNodeVehicleDestination(ship)}
    </Liner>
  }

  def createNodeCruiseBoat(ship: Ship) : scala.xml.Node = {
    <CruiseBoat
    level={ship.level.toString}
    fuel={ship.fuelLevel.toString}
    passenger={ship.nbPassenger().toString}
    >
      {createNodeVehicleLocation(ship)}
      {createNodeVehicleGoal(ship)}
      {createNodeVehicleDestination(ship)}
    </CruiseBoat>
  }

  def createNodeVehicleLocation(vehicle: Vehicle) : scala.xml.Node = {
        <Location>
          {vehicle.currentTransportFacility match {
            case Some(transportFacility) => <Town name={transportFacility.town.name} />
            case _ => vehicle.currentRoad match {
              case Some(road) => createNodeRoad(road)
              case _ => //TODO exception
              }
            }
          }
        </Location>
  }


  def createNodeVehicleGoal(vehicle: Vehicle) : scala.xml.NodeSeq = {
        vehicle.goalTransportFacility match {
          case Some(transportFacility) => <Goal name={transportFacility.town.name}/>
          case _ => NodeSeq.Empty
        }
  }

  def createNodeVehicleDestination(vehicle: Vehicle) : scala.xml.NodeSeq = {
        vehicle.destination match {
          case Some(transportFacility) => <Destination name={transportFacility.town.name}/>
          case _ => NodeSeq.Empty
          }
  }

  def createNodeVehicle (vehicle: Vehicle) : scala.xml.Node = {
    vehicle match {
      case train : Train => vehicle.vehicleType.name match {
        case "Diesel Train" => createNodeDieselTrain(train)
        case "Electric Train" => createNodeElectricTrain(train)
      }

      case plane : Plane => vehicle.vehicleType.name match {
        case "Boeing" => createNodeBoeing(plane)
        case "Concorde" => createNodeConcorde(plane)
      }

      case truck : Truck => createNodeTruck(truck)

      case ship : Ship => vehicle.vehicleType.name match {
        case "Liner" => createNodeLiner(ship)
        case "Cruise Boat" => createNodeCruiseBoat(ship)
      }
    }
  }

  def createNodeRoad (road : Road) : scala.xml.Node = {
    road.roadType.name match {
      case "Rail" => <Rail
        townA={road.transportFacilityA.town.name}
        townB={road.transportFacilityB.town.name}
        />

      case "Line" => <Line
        townA={road.transportFacilityA.town.name}
        townB={road.transportFacilityB.town.name}
        />

      case "Waterway" => <Waterway
        townA={road.transportFacilityA.town.name}
        townB={road.transportFacilityB.town.name}
        />

      case "Highway" => <Highway
        townA={road.transportFacilityA.town.name}
        townB={road.transportFacilityB.town.name}
        />
    }
  }

  def createNodeNaturalWaterway (naturalWaterway: World#NaturalWaterway) : scala.xml.Node = {
      <NaturalWaterway townA={naturalWaterway.townA.name} townB={naturalWaterway.townB.name}/>
  }

  def createNodeCity (town : Town) : scala.xml.Node = {
    <City name={town.name} x={town.pos.x.toString} y={town.pos.y.toString} population={town.population.toString}>
      <Warehouse>
        {town.warehouse.packs.foldLeft(scala.xml.NodeSeq.Empty)((acc, resourcePack) => acc++createNodeResourcePack(resourcePack))}
      </Warehouse>
      <Offer>
        {town.offer.packs.foldLeft(scala.xml.NodeSeq.Empty)((acc, resourcePack) => acc++createNodeResourcePack(resourcePack))}
      </Offer>
      <Request>
        {town.requests.resources.foldLeft(scala.xml.NodeSeq.Empty)((acc, resourcePack) => acc++createNodeRequest(resourcePack))}
      </Request>
      <Consumption>
        {town.consumption.resources.foldLeft(scala.xml.NodeSeq.Empty)((acc, resourcePack) => acc++createNodeConsumption(resourcePack))}
      </Consumption>
      {town.factories.foldLeft(scala.xml.NodeSeq.Empty)((acc,factory) => acc++createNodeFactory(factory))}
      {if (town.hasAirport) <Airport level={town.airport.get.level.toString}/>}
      {if (town.hasHarbor) <Port level={town.harbor.get.level.toString}/>}
      {if (town.hasGasStation) <GasStation level={town.gasStation.get.level.toString}/>}
      {if (town.hasStation) <Station level={town.station.get.level.toString}/>}
    </City>
  }

  def createNode : scala.xml.Node = {
    <Map>
      <Date days={world.gameDateTime.days.toString} hours={world.gameDateTime.hours.toString}/>
      <Money>{world.company.money}</Money>
      {world.towns.foldLeft(scala.xml.NodeSeq.Empty)((acc, town) => acc++createNodeCity(town))}
      {world.naturalWaterways.foldLeft(scala.xml.NodeSeq.Empty)((acc, naturalWaterway) => acc++createNodeNaturalWaterway(naturalWaterway))}
      <Connection>
        {world.company.roads.foldLeft(scala.xml.NodeSeq.Empty)((acc, road) => acc++createNodeRoad(road))}
      </Connection>
      <Vehicle>
        {world.company.vehicles.foldLeft(scala.xml.NodeSeq.Empty)((acc, vehicle) => acc++createNodeVehicle(vehicle))}
      </Vehicle>
    </Map>
  }

}

/*

<Statistic>
        {town.stats.events.foldLeft(scala.xml.NodeSeq.Empty)((acc, event) => acc++createNodeEvent(event))}
      </Statistic>

 */