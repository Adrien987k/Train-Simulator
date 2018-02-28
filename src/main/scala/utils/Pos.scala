package utils

class Pos(private var _x: Int, private var _y: Int) extends Comparable[Pos] {

  def x : Int = _x
  def y : Int = _y

  def x_= (value:Int):Unit = _x = value
  def y_= (value:Int):Unit = _y = value

  def inRange(pos2 : Pos, range : Int) : Boolean = {
    val dist = math.sqrt((pos2.x - x) * (pos2.x - x) +
                     (pos2.y - y) * (pos2.y - y))
    dist <= range
  }

  override def compareTo(o: Pos): Int = {
    if (x == o.x && y == o.y) return 0
    1
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case pos : Pos => compareTo(pos) == 0
      case _ => false
    }
  }
}
