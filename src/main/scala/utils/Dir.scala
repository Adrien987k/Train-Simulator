package utils

class Dir(private var _x : Int, private var _y : Int) {

  def x : Int = _x
  def y : Int = _y

  def x_= (value:Int):Unit = _x = value
  def y_= (value:Int):Unit = _y = value

}
