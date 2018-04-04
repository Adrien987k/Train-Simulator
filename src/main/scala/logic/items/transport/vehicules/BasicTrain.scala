package logic.items.transport.vehicules

import game.Game
import logic.world.Company
import interface.WorldCanvas
import logic.items.ItemTypes.TrainType
import logic.items.transport.facilities.Station
import logic.items.transport.vehicules.components.{Carriage, Engine}

import scalafx.Includes._
import scala.collection.mutable.ListBuffer
import scalafx.event.ActionEvent
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, VBox}
import scalafx.scene.text.{Font, FontWeight}

class BasicTrain
(trainType : TrainType,
 company : Company,
 station : Station,
 engine : Engine,
 carriages : ListBuffer[Carriage])
  extends Train(trainType, company, engine, carriages, station) {

}
