package engine

/* NOT USED YET */
object Resources {

  sealed abstract class Resource(
    val cost : Int,
    val size : Int,
    val weight : Int,
    val name : String
                                ) {



  }

  case object WOOD extends Resource(5, 5, 10, "wood")

}
