package utils

class Pos(private var _x: Int, private var _y: Int) extends Comparable[Pos] {

  def x : Int = _x
  def y : Int = _y

  def x_= (value:Int):Unit = _x = value
  def y_= (value:Int):Unit = _y = value


  def dist(pos : Pos) : Int = {
    math.sqrt((pos.x - x) * (pos.x - x) +
    (pos.y - y) * (pos.y - y)).toInt
  }

  def inRange(pos : Pos, range : Int) : Boolean = {
    dist(pos) <= range
  }

  def inLineRange(pos1 : Pos, pos2 : Pos, range : Double) : Boolean = {
      (pos2.x - pos1.x) != 0 && (x - pos1.x) != 0 &&
      math.abs(((pos2.y - pos1.y).toDouble / (pos2.x - pos1.x).toDouble) -
               ((y - pos1.y).toDouble / (x - pos1.x).toDouble)) <= (if (range != 0) 1 / range else 0.1) &&
      ((x <= pos2.x && x >= pos1.x) || (x >= pos2.x && x <= pos1.x))
  }

  def copy(): Pos = {
    new Pos(x, y)
  }

  override def compareTo(pos: Pos): Int = {
    if (x == pos.x && y == pos.y) return 0
    1
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case pos : Pos => compareTo(pos) == 0
      case _ => false
    }
  }

  override def toString: String = {
    "(" + x + ", " + y + ")"
  }
}
