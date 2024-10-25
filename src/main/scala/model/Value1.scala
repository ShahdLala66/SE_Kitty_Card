package model

object Value1 extends Enumeration {
  type Value1 = Value
  val One = Value(1)
  val Two = Value(2)
  val Three = Value(3)
  val Four = Value(4)
  val Five = Value(5)
  val Six = Value(6)
  val Seven = Value(7)
  val Eight = Value(8)

  // Helper method to convert a Value to its integer representation
  def toInt(value: Value): Int = value match {
    case One   => 1
    case Two   => 2
    case Three => 3
    case Four  => 4
    case Five  => 5
    case Six   => 6
    case Seven => 7
    case Eight => 8
  }
}