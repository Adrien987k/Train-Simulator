package utils

class Dir(private var _x : Double, private var _y : Double) {

  def x : Double = _x
  def y : Double = _y

  def x_= (value:Double):Unit = _x = value
  def y_= (value:Double):Unit = _y = value

  def normalize(): Unit = {
    val length = math.sqrt((x * x) + (y * y))
    if (length != 0) {
      x = x / length
      y = y / length
    }
  }

}
