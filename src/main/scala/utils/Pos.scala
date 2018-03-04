package utils

class Pos(private var _x: Double, private var _y: Double) extends Comparable[Pos] {

  def x : Double = _x
  def y : Double = _y

  def x_= (value:Double):Unit = _x = value
  def y_= (value:Double):Unit = _y = value


  def dist(pos : Pos) : Double = {
    math.sqrt((pos.x - x) * (pos.x - x) +
    (pos.y - y) * (pos.y - y))
  }

  def inRange(pos : Pos, range : Int) : Boolean = {
    dist(pos) <= range
  }

  def inLineRange(pos1 : Pos, pos2 : Pos, range : Double) : Boolean = {
    val distA = pos1.dist(this)
    val distB = pos2.dist(this)
    val distC = pos1.dist(pos2)
    val dist: Double = if (distB * distB > distA * distA + distC * distC)
      distA
    else if (distA * distA > distB * distB + distC * distC)
      distB
    else {
      val s = (distA + distB + distC) / 2
      (2 / distC) * math.sqrt(s * (s - distA) * (s - distB) * (s - distC))
    }
    dist <= range
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
    "(" + x.toInt + ", " + y.toInt + ")"
  }
}
