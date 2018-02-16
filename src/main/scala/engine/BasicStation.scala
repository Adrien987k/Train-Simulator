package engine

class BasicStation extends Station {

  override def place(): Unit = {

  }

  override def step(): Unit = {

  }

  override def info(): String = {
    "Name : " + name + "\n\n" +
    "Capacity : " + capacity + "\n\n"
  }
}
