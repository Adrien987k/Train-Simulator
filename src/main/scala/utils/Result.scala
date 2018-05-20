package utils

class Result

case class Success() extends Result
case class Failure(reason : String) extends Result
