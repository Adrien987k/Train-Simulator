package logic

trait Loadable {

  def load (node : scala.xml.Node) : Unit

  def save : scala.xml.NodeSeq

}