// --------------------------------------------------------------------
// Flood-It! Game
// --------------------------------------------------------------------

import java.awt.{Graphics2D,Color}
import scala.swing._

// --------------------------------------------------------------------

/* Engine */
// abstract class Train             (capacity speed)
// abstract class Town              (Number and size of stations, pos)
// object World                      (Graph of Town and Road)
// abstract class Resource          (Marchandise)
// abstract class Road              (capacity)
// class Player/Company             (2 players ?)



object ItemType extends Enumeration {
  val Train, Town, Station, Road = value
}

/*
class Resource {
  int cost
  int size
  int weight
  string name
  //Texture tex
}

class ResourcePack {
  Resource res
  int quantity
}

class Capacity {
  int size
  int weight
}

class Pos {
  int x int y
}

abstract class Item {
  abstract place()
}

class Company {
  int monyey

  ItemType selected
  //
  select(ItemType item)
  buy(ItemType item)
}

object Shop {
  int road_price
  int station_price
  int train_price
}

Trait Updatabable {
  abstract step()
}

object World {
  List<Town> graph
  List<Train> trains
  Company comp
  //
  init()
  run()
}

abstract class Train extends Item with Updatable {
  int speed
  Capacity cap
  List<ResourcePack>
  Status stats // En mouvement ? En attente de chargement ? Dechargement ?
  Pos pos
  Road road
  //
  move()
  //place()
  //step()
}

abstract class Road extends Item {
  Town t1
  Town t2
  int capacity_t1_t2
  int capacity_t2_t1
  List<Train> t1_t2
  List<Train> t1_t2
  //
  place()
}

abstract class Station extends Item with Updatable {
  int capacity
  List<Train>
  List<Road>
  //place()
  //step()
}

class Offer {
  List<ResourcePack>
}

class Request {
  List<Resource>
}

abstract class Town with Updatable {
  List<Station>
  Pos pos
  Offer offer
  Request
  //
  produce()
  unload(Train)
  explore() // Explorer les villes voisines puis charger des trains
  load(Train)
  step()
}

*/


/* Interface and Game */
// class Game
// class UI ( display(world : World) : Unit )

//Initialement, aucune route entre les villes; 20.000$
//Le joueur choisit les routes et gares à construire

//Un transport rapporte des $, Les gares et routes coutent des $



case class Pos(val x: Int, val y: Int)

class Blocks(val width: Int, val height: Int) {
  private val data = Array.ofDim[Int](width, height)
  resetData()

  // Create initial pattern
  def resetData() = {
    for (x <- 0 until width)
      for (y <- 0 until height)
	data(x)(y) = (math.random * FloodIt.NumColors).toInt
  }

  // Return color of the pixel.
  def get(pos: Pos): Int = data(pos.x)(pos.y)

  // Set color of pixel.
  def set(pos: Pos, color: Int) = {
    require(0 <= color && color <= FloodIt.NumColors)
    data(pos.x)(pos.y) = color
  }

  // Return array with neighbors of a pixel
  def neighbors(p: Pos): List[Pos] = {
    var nb = List[Pos]()
    val Pos(x, y) = p
    if (x > 0)
      nb = Pos(x-1, y) :: nb
    if (y > 0)
      nb = Pos(x, y-1) :: nb
    if (x + 1 < width)
      nb = Pos(x+1, y) :: nb
    if (y + 1 < height)
      nb = Pos(x, y+1) :: nb
    nb
  }

  def allFilled(): Boolean = {
    val col = data(0)(0)
    for (x <- 0 until width; y <- 0 until height)
      if (data(x)(y) != col)
	return false
    true
  }

  def flood(start: Pos, color: Int) {
    val A = new scala.collection.mutable.Queue[Pos]

    val oldCol = get(start)
    if (oldCol == color)
      return

    set(start, color)
    A += start

    while (!A.isEmpty) {
      val q = A.dequeue()
      val nb = neighbors(q)
      for (r <- nb) {
	if (get(r) == oldCol) {
	  set(r, color)
	  A += r
	}
      }
    }
  }
}

// --------------------------------------------------------------------

class Canvas(val blocks: Blocks) extends Component {

  override def paintComponent(g : Graphics2D) {
    val d = size
    g.setColor(Color.white);
    g.fillRect(0,0, d.width, d.height);
    val rowWid = d.height / blocks.height
    val colWid = d.width / blocks.width
    val wid = rowWid min colWid
    val x0 = (d.width - blocks.width * wid)/2
    val y0 = (d.height - blocks.height * wid)/2
    for (x <- 0 until blocks.width) {
      for (y <- 0 until blocks.height) {
	g.setColor(FloodIt.colorFor(blocks.get(Pos(x, y))))
	g.fillRect(x0 + x * wid, y0 + y * wid, wid, wid)
      }
    }
  }
}

// --------------------------------------------------------------------

class FloodUI extends MainFrame {
  val turns = new Label("Turns: 0")
  var counter = 0

  val blocks = new Blocks(FloodIt.size, FloodIt.size)
  val canvas = new Canvas(blocks)

  title = FloodIt.Title
  preferredSize = new Dimension(1080, 720)
  contents = new BorderPanel {
    border = Swing.MatteBorder(8, 8, 8, 8, Color.white)
    add(canvas, BorderPanel.Position.Center)
    val buttons = new BoxPanel(Orientation.Horizontal) {
      border = Swing.EmptyBorder(8, 0, 0, 0)
      background = Color.white
      contents += Button("New Game") { newGame() }
      contents += Swing.HGlue
      for (i <- 0 until FloodIt.NumColors) {
	val b = Button(" ") { colorClick(i) }
	b.background = FloodIt.colorFor(i)
	contents += b
      }
      contents += Swing.HGlue
      contents += turns
      contents += Swing.HGlue
      contents += Button("Quit") { System.exit(0) }
    }
    add(buttons, BorderPanel.Position.South)
  }

  private def setTurns() { turns.text = "Turns: %d".format(counter) }

  private def restartGame() {
    counter = 0
    setTurns()
    blocks.resetData()
    canvas.repaint()
  }

  def newGame() {
    if (Dialog.showConfirmation(canvas, "Do you want to start a new game",
				title=FloodIt.Title) == Dialog.Result.Ok)
      restartGame()
  }

  def colorClick(color: Int) {
    if (color == blocks.get(Pos(0,0))) {
      Dialog.showMessage(canvas, "You must be kidding!", title=FloodIt.Title)
    } else {
      counter += 1
      setTurns()
      blocks.flood(Pos(0,0), color)
      canvas.repaint()
      if (blocks.allFilled()) {
	val s = "Congratulations, you made it in %d turns".format(counter)
	Dialog.showMessage(canvas, s, title=FloodIt.Title)
	restartGame()
      }
    }
  }
}

object FloodIt {
  var size = 19
  val NumColors = 6
  val Title = "Flood It!"

  def colorFor(n: Int): Color = {
    n match {
      case 0 => Color.red
      case 1 => Color.green
      case 2 => Color.blue
      case 3 => Color.yellow
      case 4 => Color.pink
      case 5 => Color.cyan
      case _ => Color.white // should not happen
    }
  }

  def main(args: Array[String]) {
    if (args.length == 1)
      size = args(0).toInt

    val ui = new FloodUI
    ui.visible = true
  }
}

// --------------------------------------------------------------------
