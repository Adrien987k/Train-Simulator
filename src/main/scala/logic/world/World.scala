package logic.world

import game.Game.world
import logic.{Loadable, Updatable}
import logic.world.towns.Town
import interface.{GUI, ItemsStyle}
import logic.economy.{Cargo, ResourcePack, Resources}
import logic.items.transport.facilities.TransportFacilityFactory
import logic.items.transport.facilities.TransportFacilityTypes.{AIRPORT, GAS_STATION, HARBOR, STATION}
import logic.items.transport.roads.RoadTypes.{HIGHWAY, LINE, RAIL, WATERWAY}
import logic.items.transport.vehicules._
import logic.items.transport.vehicules.VehicleTypes._
import logic.items.transport.vehicules.components.{Carriage, TrainComponentFactory}
import utils.{DateTime, Pos}

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.xml.Node
import scalafx.animation.AnimationTimer

class World() extends Loadable{

  class NaturalWaterway
  (val townA : Town,
   val townB : Town) extends Loadable {
    override def load(node: Node): Unit = {

    }

    override def save: Node = {
        <NaturalWaterway townA={townA.name} townB={townB.name}/>
    }
  }

  private val MAP_WIDTH = 2000
  private val MAP_HEIGHT = 2000

  private val INIT_NB_TOWNS = 50
  private val APPARITION_WATERWAY = 0.5

  private val rand = new Random

  var towns : ListBuffer[Town] = ListBuffer.empty
  var naturalWaterways : ListBuffer[NaturalWaterway] = ListBuffer.empty

  var company : Company = new Company(this)

  val gameDateTime : DateTime = new DateTime

  def start() : Unit = {
    init()

    GUI.initWorldCanvas(towns)

    val timer = AnimationTimer { _ =>
      update()

      GUI.update()

      Thread.sleep(50)
    }

    timer.start()
  }

  def init() : Unit = {
    towns = ListBuffer.empty
    naturalWaterways = ListBuffer.empty

    company = new Company(this)

    generateRandomMap()

    gameDateTime.restart()
  }

  override def load(node : scala.xml.Node) : Unit = {
    loadWorld(node)

    GUI.initWorldCanvas(towns)

    val timer = AnimationTimer { _ =>
      update()

      GUI.update()

      Thread.sleep(50)
    }

    timer.start()
  }

  override def save : Node = {
    <Map>
      <Date days={this.gameDateTime.days.toString} hours={this.gameDateTime.hours.toString}/>
      <Money>{this.company.money}</Money>
      {this.towns.foldLeft(scala.xml.NodeSeq.Empty)((acc, town) => acc++town.save)}
      {this.naturalWaterways.foldLeft(scala.xml.NodeSeq.Empty)((acc, naturalWaterway) => acc++naturalWaterway.save)}
      <Connection>
        {world.company.roads.foldLeft(scala.xml.NodeSeq.Empty)((acc, road) => acc++road.save)}
      </Connection>
      <Vehicle>
        {world.company.vehicles.foldLeft(scala.xml.NodeSeq.Empty)((acc, vehicle) => acc++vehicle.save)}
      </Vehicle>
    </Map>
  }

  def update() : Unit = {
    gameDateTime.update()

    company.step()
    towns.foreach(_.step())
  }

  def time() : DateTime = gameDateTime.copy()

  def loadWorld(node : scala.xml.Node) : Unit = {
    towns = ListBuffer.empty
    naturalWaterways = ListBuffer.empty

    company = new Company(this)

    for (<Money>{money}</Money> <- node \"Money") {company._money = money.text.toDouble}

    generateLoadMap(node)
    gameDateTime.load(node \"Date")
  }

  /**
    * @return True if there exists a natural waterway between [townA] and [townB]
    */
  def existNaturalWaterWay(townA : Town, townB : Town) : Boolean = {
    naturalWaterways.exists(waterway => {
      (waterway.townA == townA && waterway.townB == townB) ||
      (waterway.townB == townA && waterway.townA == townB)
    })
  }

  /**
    * Generate a random new map
    */
  def generateRandomMap() : Unit = {
    val areaWidth = MAP_WIDTH / INIT_NB_TOWNS

    for (i <- 0 to INIT_NB_TOWNS) {
      val x = rand.nextInt(areaWidth - ItemsStyle.INIT_TOWN_SIZE * 2) + (i * areaWidth) + ItemsStyle.INIT_TOWN_SIZE
      val y = rand.nextInt(MAP_HEIGHT - ItemsStyle.INIT_TOWN_SIZE * 2) + ItemsStyle.INIT_TOWN_SIZE
      towns += new Town(new Pos(x, y), "Town " + i)
    }

    for (townA <- towns) {
      for (townB <- towns) {
        if (townA != townB) {
          val randDouble = rand.nextDouble() * 100.0

          if (randDouble < APPARITION_WATERWAY && townA != townB && !existNaturalWaterWay(townA, townB)) {
            naturalWaterways += new NaturalWaterway(townA, townB)
          }
        }
      }
    }
  }

  def loadNaturalWaterway(nameTownA : String, nameTownB : String) : Unit = {
    towns.foreach(townA => if (nameTownA == townA.name) {towns.foreach(townB => if (nameTownB == townB.name) {naturalWaterways += new NaturalWaterway(townA,townB)})} )
  }

  def loadConnection(road : scala.xml.Node, townA : Town, townB : Town) : Unit = {
    road match {
      case <Rail></Rail> =>
        val transportFacilityA = TransportFacilityFactory.make(STATION, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(STATION, company, townB)
        company.buildRoad(RAIL, transportFacilityA, transportFacilityB)

      case <Line></Line> =>
        val transportFacilityA = TransportFacilityFactory.make(AIRPORT, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(AIRPORT, company, townB)
        company.buildRoad(LINE, transportFacilityA, transportFacilityB)

      case <Waterway></Waterway> =>
        val transportFacilityA = TransportFacilityFactory.make(HARBOR, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(HARBOR, company, townB)
        company.buildRoad(WATERWAY, transportFacilityA, transportFacilityB)

      case <Highway></Highway> =>
        val transportFacilityA = TransportFacilityFactory.make(GAS_STATION, company, townA)
        val transportFacilityB = TransportFacilityFactory.make(GAS_STATION, company, townB)
        company.buildRoad(HIGHWAY, transportFacilityA, transportFacilityB)
    }
  }

  def loadTruck(node : scala.xml.Node) : Unit = {
    val level = (node \"@level").text.toInt
    val fuel = (node \"@fuel").text.toDouble
    for (<InitialLocation>{subnode @ _*}</InitialLocation> <- node \"InitialLocation") {
      for (initLocation <- subnode) {initLocation match {
        case <Town>{_*}</Town> =>
          towns.foreach(town => if ((initLocation \"@name").text == town.name) {
            val newTruck = VehicleFactory.make(TRUCK, company, town.gasStation.get).asInstanceOf[Truck]
            company.addVehicle(newTruck)
            newTruck.setFuelLevel(fuel)
            newTruck.level = level
            for (location @ <Town>{_*}</Town> <- node \"Location") { location match {
              case <Town>{_*}</Town> =>
                towns.foreach(city => if ((location \"@name").text == city.name) {
                  newTruck.currentTransportFacility = Some(city.gasStation.get)
                  city.gasStation.get.addVehicle(newTruck)
                })
              case <Highway>{_*}</Highway> => towns.foreach(townA => if ((location \"@townA").text == townA.name) {
                towns.foreach(townB => if ((location \"@townB").text == townB.name) {
                  company.roads.foreach(road => if (road.roadType == HIGHWAY && road.transportFacilityA == townA.gasStation.get && road.transportFacilityB == townB.gasStation.get) {
                    road.addVehicle(newTruck)
                    newTruck.currentRoad = Some(road)
                  })
                })
              })
            }}
            for (goal <- node \"Goal") {goal match {
              case <Goal>{_*}</Goal> => towns.foreach(city => if ((goal \"@name").text == city.name) {
                newTruck.goalTransportFacility = Some(city.gasStation.get)
              })
              case _ => ()
            }}
            for (destination <- node \"Destination") {destination match {
              case <Destination>{_*}</Destination> => towns.foreach(city => if ((destination \"@name").text == city.name) {
                newTruck.destination = Some(city.gasStation.get)
              })
              case _ => ()
            }}
            for (cargo <- node \"Cargo") {cargo match {
              case <Cargo>{resources @ _*}</Cargo> => val listResourcePack : ListBuffer[ResourcePack] = ListBuffer.empty
                for (resource @ <Resource>{_*}</Resource> <- resources) {
                  val resourceType = (resource \"@type").text
                  val quantity = (resource \"@quantity").text.toInt
                  listResourcePack += Resources.loadResource(resourceType, quantity)
                }
                val newCargo = new Cargo(listResourcePack.head.resource.resourceType)
                newCargo.resources.storeResourcePacks(listResourcePack)
                newTruck.cargoOpt = Some(newCargo)
            }}
          })
        case _ => ()
      }
  }}}

  def loadBoeing(node: scala.xml.Node) : Unit = {
    val level = (node \"@level").text.toInt
    val fuel = (node \"@fuel").text.toDouble
    val passenger = (node \"@passenger").text.toInt
    for (<InitialLocation>{subnode @ _*}</InitialLocation> <- node \"InitialLocation") {
      for (initLocation <- subnode) {initLocation match {
        case <Town>{_*}</Town> =>
          towns.foreach(town => if ((initLocation \"@name").text == town.name) {
            val newBoeing = VehicleFactory.make(BOEING, company, town.airport.get).asInstanceOf[Plane]
            company.addVehicle(newBoeing)
            newBoeing.setFuelLevel(fuel)
            newBoeing.level = level
            newBoeing.loadPassenger(passenger)
            for (location @ <Town>{_*}</Town> <- node \"Location") { location match {
              case <Town>{_*}</Town> =>
                towns.foreach(city => if ((location \"@name").text == city.name) {
                  newBoeing.currentTransportFacility = Some(city.airport.get)
                  city.airport.get.addVehicle(newBoeing)
                })
              case <Line>{_*}</Line> => towns.foreach(townA => if ((location \"@townA").text == townA.name) {
                towns.foreach(townB => if ((location \"@townB").text == townB.name) {
                  company.roads.foreach(road => if (road.roadType == LINE && road.transportFacilityA == townA.airport.get && road.transportFacilityB == townB.airport.get) {
                    road.addVehicle(newBoeing)
                    newBoeing.currentRoad = Some(road)
                  })
                })
              })
            }}
            for (goal <- node \"Goal") {goal match {
              case <Goal>{_*}</Goal> => towns.foreach(city => if ((goal \"@name").text == city.name) {
                newBoeing.goalTransportFacility = Some(city.airport.get)
              })
              case _ => ()
            }}
            for (destination <- node \"Destination") {destination match {
              case <Destination>{_*}</Destination> => towns.foreach(city => if ((destination \"@name").text == city.name) {
                newBoeing.destination = Some(city.airport.get)
              })
              case _ => ()
            }}
          })
        case _ => ()
      }
      }}
  }

  def loadConcorde(node: scala.xml.Node) : Unit = {
    val level = (node \"@level").text.toInt
    val fuel = (node \"@fuel").text.toDouble
    val passenger = (node \"@passenger").text.toInt
    for (<InitialLocation>{subnode @ _*}</InitialLocation> <- node \"InitialLocation") {
      for (initLocation <- subnode) {initLocation match {
        case <Town>{_*}</Town> =>
          towns.foreach(town => if ((initLocation \"@name").text == town.name) {
            val newConcorde = VehicleFactory.make(CONCORDE, company, town.airport.get).asInstanceOf[Plane]
            company.addVehicle(newConcorde)
            newConcorde.setFuelLevel(fuel)
            newConcorde.level = level
            newConcorde.loadPassenger(passenger)
            for (location @ <Town>{_*}</Town> <- node \"Location") { location match {
              case <Town>{_*}</Town> =>
                towns.foreach(city => if ((location \"@name").text == city.name) {
                  newConcorde.currentTransportFacility = Some(city.airport.get)
                  city.airport.get.addVehicle(newConcorde)
                })
              case <Line>{_*}</Line> => towns.foreach(townA => if ((location \"@townA").text == townA.name) {
                towns.foreach(townB => if ((location \"@townB").text == townB.name) {
                  company.roads.foreach(road => if (road.roadType == LINE && road.transportFacilityA == townA.airport.get && road.transportFacilityB == townB.airport.get) {
                    road.addVehicle(newConcorde)
                    newConcorde.currentRoad = Some(road)
                  })
                })
              })
            }}
            for (goal <- node \"Goal") {goal match {
              case <Goal>{_*}</Goal> => towns.foreach(city => if ((goal \"@name").text == city.name) {
                newConcorde.goalTransportFacility = Some(city.airport.get)
              })
              case _ => ()
            }}
            for (destination <- node \"Destination") {destination match {
              case <Destination>{_*}</Destination> => towns.foreach(city => if ((destination \"@name").text == city.name) {
                newConcorde.destination = Some(city.airport.get)
              })
              case _ => ()
            }}
          })
        case _ => ()
      }
      }}
  }

  def loadCruiseBoat(node: scala.xml.Node) : Unit = {
    val level = (node \"@level").text.toInt
    val fuel = (node \"@fuel").text.toDouble
    val passenger = (node \"@passenger").text.toInt
    for (<InitialLocation>{subnode @ _*}</InitialLocation> <- node \"InitialLocation") {
      for (initLocation <- subnode) {initLocation match {
        case <Town>{_*}</Town> =>
          towns.foreach(town => if ((initLocation \"@name").text == town.name) {
            val newCruiseBoat = VehicleFactory.make(CRUISE_BOAT, company, town.harbor.get).asInstanceOf[Ship]
            company.addVehicle(newCruiseBoat)
            newCruiseBoat.setFuelLevel(fuel)
            newCruiseBoat.level = level
            newCruiseBoat.loadPassenger(passenger)
            for (location @ <Town>{_*}</Town> <- node \"Location") { location match {
              case <Town>{_*}</Town> =>
                towns.foreach(city => if ((location \"@name").text == city.name) {
                  newCruiseBoat.currentTransportFacility = Some(city.harbor.get)
                  city.harbor.get.addVehicle(newCruiseBoat)
                })
              case <Waterway>{_*}</Waterway> => towns.foreach(townA => if ((location \"@townA").text == townA.name) {
                towns.foreach(townB => if ((location \"@townB").text == townB.name) {
                  company.roads.foreach(road => if (road.roadType == WATERWAY && road.transportFacilityA == townA.harbor.get && road.transportFacilityB == townB.harbor.get) {
                    road.addVehicle(newCruiseBoat)
                    newCruiseBoat.currentRoad = Some(road)
                  })
                })
              })
            }}
            for (goal <- node \"Goal") {goal match {
              case <Goal>{_*}</Goal> => towns.foreach(city => if ((goal \"@name").text == city.name) {
                newCruiseBoat.goalTransportFacility = Some(city.harbor.get)
              })
              case _ => ()
            }}
            for (destination <- node \"Destination") {destination match {
              case <Destination>{_*}</Destination> => towns.foreach(city => if ((destination \"@name").text == city.name) {
                newCruiseBoat.destination = Some(city.harbor.get)
              })
              case _ => ()
            }}
          })
        case _ => ()
      }
      }}
  }

  def loadLiner(node : scala.xml.Node) : Unit = {
    val level = (node \"@level").text.toInt
    val fuel = (node \"@fuel").text.toDouble
    for (<InitialLocation>{subnode @ _*}</InitialLocation> <- node \"InitialLocation") {
      for (initLocation <- subnode) {initLocation match {
        case <Town>{_*}</Town> =>
          towns.foreach(town => if ((initLocation \"@name").text == town.name) {
            val newLiner = VehicleFactory.make(LINER, company, town.harbor.get).asInstanceOf[Ship]
            company.addVehicle(newLiner)
            newLiner.setFuelLevel(fuel)
            newLiner.level = level
            for (location @ <Town>{_*}</Town> <- node \"Location") { location match {
              case <Town>{_*}</Town> =>
                towns.foreach(city => if ((location \"@name").text == city.name) {
                  newLiner.currentTransportFacility = Some(city.harbor.get)
                  city.harbor.get.addVehicle(newLiner)
                })
              case <Waterway>{_*}</Waterway> => towns.foreach(townA => if ((location \"@townA").text == townA.name) {
                towns.foreach(townB => if ((location \"@townB").text == townB.name) {
                  company.roads.foreach(road => if (road.roadType == WATERWAY && road.transportFacilityA == townA.harbor.get && road.transportFacilityB == townB.harbor.get) {
                    road.addVehicle(newLiner)
                    newLiner.currentRoad = Some(road)
                  })
                })
              })
            }}
            for (goal <- node \"Goal") {goal match {
              case <Goal>{_*}</Goal> => towns.foreach(city => if ((goal \"@name").text == city.name) {
                newLiner.goalTransportFacility = Some(city.harbor.get)
              })
              case _ => ()
            }}
            for (destination <- node \"Destination") {destination match {
              case <Destination>{_*}</Destination> => towns.foreach(city => if ((destination \"@name").text == city.name) {
                newLiner.destination = Some(city.harbor.get)
              })
              case _ => ()
            }}
            for (cargo <- node \"Cargo") {cargo match {
              case <Cargo>{resources @ _*}</Cargo> => val listResourcePack : ListBuffer[ResourcePack] = ListBuffer.empty
                for (resource @ <Resource>{_*}</Resource> <- resources) {
                  val resourceType = (resource \"@type").text
                  val quantity = (resource \"@quantity").text.toInt
                  listResourcePack += Resources.loadResource(resourceType, quantity)
                }
                val newCargo = new Cargo(listResourcePack.head.resource.resourceType)
                newCargo.resources.storeResourcePacks(listResourcePack)
                newLiner.cargoes += newCargo
            }}
          })
        case _ => ()
      }
      }}}

  def loadDieselTrain(node : scala.xml.Node) : Unit = {
    val level = (node \"@level").text.toInt
    val fuel = (node \"@fuel").text.toDouble
    val levelEngine = (node \"@levelengine").text.toInt
    for (<InitialLocation>{subnode @ _*}</InitialLocation> <- node \"InitialLocation") {
      for (initLocation <- subnode) {initLocation match {
        case <Town>{_*}</Town> =>
          towns.foreach(town => if ((initLocation \"@name").text == town.name) {
            val newDieselTrain = VehicleFactory.make(DIESEL_TRAIN, company, town.station.get).asInstanceOf[Train]
            company.addVehicle(newDieselTrain)
            newDieselTrain.engine.setFuelLevel(fuel)
            newDieselTrain.engine.level = levelEngine
            newDieselTrain.level = level
            for (location @ <Town>{_*}</Town> <- node \"Location") { location match {
              case <Town>{_*}</Town> =>
                towns.foreach(city => if ((location \"@name").text == city.name) {
                  newDieselTrain.currentTransportFacility = Some(city.station.get)
                  city.station.get.addVehicle(newDieselTrain)
                })
              case <Rail>{_*}</Rail> => towns.foreach(townA => if ((location \"@townA").text == townA.name) {
                towns.foreach(townB => if ((location \"@townB").text == townB.name) {
                  company.roads.foreach(road => if (road.roadType == RAIL && road.transportFacilityA == townA.station.get && road.transportFacilityB == townB.station.get) {
                    road.addVehicle(newDieselTrain)
                    newDieselTrain.currentRoad = Some(road)
                  })
                })
              })
            }}
            for (goal <- node \"Goal") {goal match {
              case <Goal>{_*}</Goal> => towns.foreach(city => if ((goal \"@name").text == city.name) {
                newDieselTrain.goalTransportFacility = Some(city.station.get)
              })
              case _ => ()
            }}
            for (destination <- node \"Destination") {destination match {
              case <Destination>{_*}</Destination> => towns.foreach(city => if ((destination \"@name").text == city.name) {
                newDieselTrain.destination = Some(city.station.get)
              })
              case _ => ()
            }}
            val listCarriage : ListBuffer[Carriage] = ListBuffer.empty
            node match {
              case <DieselTrain>{subnode @ _*}</DieselTrain> => for (carriage <- subnode) { val levelCarriage = (carriage\"@level").text.toInt
                carriage match {
                case <PassengerCarriage>{_*}</PassengerCarriage> => val passenger = (carriage \ "@passenger").text.toInt
                  val newPassengerCarriage = TrainComponentFactory.makePassengerCarriage(DIESEL_TRAIN, company)
                  newPassengerCarriage.level = levelCarriage
                  newPassengerCarriage.nbPassenger = passenger
                  listCarriage += newPassengerCarriage
                case <ResourceCarriage>{cargoNodeSeq @ _*}</ResourceCarriage> => val listCargo : ListBuffer[Cargo] = ListBuffer.empty
                  for (<Cargo>{resources @ _*}</Cargo> <- cargoNodeSeq) {
                  val listResourcePack : ListBuffer[ResourcePack] = ListBuffer.empty
                  for (resource @ <Resource>{_*}</Resource> <- resources) {
                    val resourceType = (resource \"@type").text
                    val quantity = (resource \"@quantity").text.toInt
                    listResourcePack += Resources.loadResource(resourceType, quantity)
                  }
                  val newCargo = new Cargo(listResourcePack.head.resource.resourceType)
                  newCargo.resources.storeResourcePacks(listResourcePack)
                  listCargo += newCargo
                  }
                  val newResourceCarriage = TrainComponentFactory.makeResourceCarriage(DIESEL_TRAIN, listCargo.head.resourceType, company)
                  newResourceCarriage.level = levelCarriage
                  newResourceCarriage.cargos = listCargo
                  listCarriage += newResourceCarriage
                }
              }
            }
            newDieselTrain.carriages.clear()
            newDieselTrain.carriages ++= listCarriage
          })
        case _ => ()
      }
      }}}

  def loadElectricTrain(node : scala.xml.Node) : Unit = {
    val level = (node \"@level").text.toInt
    val fuel = (node \"@fuel").text.toDouble
    val levelEngine = (node \"@levelengine").text.toInt
    for (<InitialLocation>{subnode @ _*}</InitialLocation> <- node \"InitialLocation") {
      for (initLocation <- subnode) {initLocation match {
        case <Town>{_*}</Town> =>
          towns.foreach(town => if ((initLocation \"@name").text == town.name) {
            val newElectricTrain = VehicleFactory.make(ELECTRIC_TRAIN, company, town.station.get).asInstanceOf[Train]
            company.addVehicle(newElectricTrain)
            newElectricTrain.engine.setFuelLevel(fuel)
            newElectricTrain.engine.level = levelEngine
            newElectricTrain.level = level
            for (location @ <Town>{_*}</Town> <- node \"Location") { location match {
              case <Town>{_*}</Town> =>
                towns.foreach(city => if ((location \"@name").text == city.name) {
                  newElectricTrain.currentTransportFacility = Some(city.station.get)
                  city.station.get.addVehicle(newElectricTrain)
                })
              case <Rail>{_*}</Rail> => towns.foreach(townA => if ((location \"@townA").text == townA.name) {
                towns.foreach(townB => if ((location \"@townB").text == townB.name) {
                  company.roads.foreach(road => if (road.roadType == RAIL && road.transportFacilityA == townA.station.get && road.transportFacilityB == townB.station.get) {
                    road.addVehicle(newElectricTrain)
                    newElectricTrain.currentRoad = Some(road)
                  })
                })
              })
            }}
            for (goal <- node \"Goal") {goal match {
              case <Goal>{_*}</Goal> => towns.foreach(city => if ((goal \"@name").text == city.name) {
                newElectricTrain.goalTransportFacility = Some(city.station.get)
              })
              case _ => ()
            }}
            for (destination <- node \"Destination") {destination match {
              case <Destination>{_*}</Destination> => towns.foreach(city => if ((destination \"@name").text == city.name) {
                newElectricTrain.destination = Some(city.station.get)
              })
              case _ => ()
            }}
            val listCarriage : ListBuffer[Carriage] = ListBuffer.empty
            node match {
              case <DieselTrain>{subnode @ _*}</DieselTrain> => for (carriage <- subnode) { val levelCarriage = (carriage\"@level").text.toInt
                carriage match {
                  case <PassengerCarriage>{_*}</PassengerCarriage> => val passenger = (carriage \ "@passenger").text.toInt
                    val newPassengerCarriage = TrainComponentFactory.makePassengerCarriage(ELECTRIC_TRAIN, company)
                    newPassengerCarriage.level = levelCarriage
                    newPassengerCarriage.nbPassenger = passenger
                    listCarriage += newPassengerCarriage
                  case <ResourceCarriage>{cargoNodeSeq @ _*}</ResourceCarriage> => val listCargo : ListBuffer[Cargo] = ListBuffer.empty
                    for (<Cargo>{resources @ _*}</Cargo> <- cargoNodeSeq) {
                      val listResourcePack : ListBuffer[ResourcePack] = ListBuffer.empty
                      for (resource @ <Resource>{_*}</Resource> <- resources) {
                        val resourceType = (resource \"@type").text
                        val quantity = (resource \"@quantity").text.toInt
                        listResourcePack += Resources.loadResource(resourceType, quantity)
                      }
                      val newCargo = new Cargo(listResourcePack.head.resource.resourceType)
                      newCargo.resources.storeResourcePacks(listResourcePack)
                      listCargo += newCargo
                    }
                    val newResourceCarriage = TrainComponentFactory.makeResourceCarriage(ELECTRIC_TRAIN, listCargo.head.resourceType, company)
                    newResourceCarriage.level = levelCarriage
                    newResourceCarriage.cargos = listCargo
                    listCarriage += newResourceCarriage
                }
              }
            }
            newElectricTrain.carriages.clear()
            newElectricTrain.carriages ++= listCarriage
          })
        case _ => ()
      }
      }}}

  def generateLoadMap(node : scala.xml.Node) : Unit = {
    node match {
      case <Map>{subnode @ _*}</Map> =>
        for (city @ <City>{_*}</City> <- subnode) {
          val x = (city \"@x").text.toDouble
          val y = (city \"@y").text.toDouble
          val name = (city \"@name").text
          val population = (city \"@population").text.toInt
          val town = new Town(new Pos(x, y), name)
          town.initPopulation(population)
          town.loadCity(city, company)
          towns += town
        }
        for (waterway @ <NaturalWaterway>{_*}</NaturalWaterway> <- subnode) {
          val nameTownA = (waterway \"@townA").text
          val nameTownB = (waterway \"@townB").text
          loadNaturalWaterway(nameTownA, nameTownB)
        }
        for (<Connection>{road @ _*}</Connection> <- subnode) {
          for (nodeRoad <- road) {
            val nameTownA = (nodeRoad \"@townA").text
            val nameTownB = (nodeRoad \"@townB").text
            towns.foreach(townA => if (nameTownA == townA.name) {towns.foreach(townB => if (nameTownB == townB.name) {loadConnection(nodeRoad, townA, townB)})} )
          }
        }
        for (<Vehicle>{vehicle @ _*}</Vehicle> <- subnode) {
          for (nodeVehicle <- vehicle) { nodeVehicle match {
            case <Truck>{_*}</Truck> => loadTruck(nodeVehicle)
            case <DieselTrain>{_*}</DieselTrain> => loadDieselTrain(nodeVehicle)
            case <ElectricTrain>{_*}</ElectricTrain> => loadElectricTrain(nodeVehicle)
            case <Boeing>{_*}</Boeing> => loadBoeing(nodeVehicle)
            case <Concorde>{_*}</Concorde> => loadConcorde(nodeVehicle)
            case <Liner>{_*}</Liner> => loadLiner(nodeVehicle)
            case <CruiseBoat>{_*}</CruiseBoat> => loadCruiseBoat(nodeVehicle)
            case _ => ()
            }
          }
        }
    }
  }


  /**
    * Try to find an element at the position [pos] in the world map
    *
    * @param pos The position where to search
    * @return An option with some element if one is found none otherwise
    */
  def updatableAt(pos : Pos) : Option[Updatable] = {
    val town = towns.find(town => {
      val style = ItemsStyle.ofTown(town)

      town.pos.inRange(pos, style.radius * 1.7)
    })
    if (town.nonEmpty) return town

    val vehicle = company.vehicles.find(vehicle => {
      val style = ItemsStyle.ofVehicle(vehicle.vehicleType)
      vehicle.pos.inRange(pos, style.radius)
    })

    if (vehicle.nonEmpty) return vehicle

    company.roads.find(road => {
      val style = ItemsStyle.ofRoad(road.roadType)

      !style.empty && pos.inLineRange(road.transportFacilityA.pos, road.transportFacilityB.pos, 10)
    })
  }


  /**
    * @return The total population size of the world
    */
  def totalPopulation() : Int = {
    val inTowns = towns.foldLeft(0)((total, town) => total + town.population + town.nbWaitingPassengers)
    val inTrains = company.vehicles.foldLeft(0)((total, vehicle) => total + vehicle.nbPassenger())
    inTowns + inTrains
  }

}
