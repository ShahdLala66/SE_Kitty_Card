package model

enum Suit(val color: String) {
    case Green extends Suit("Green")
    case Brown extends Suit("Brown")
    case Purple extends Suit("Purple")
    case Blue extends Suit("Blue")
    // TODO complete and check the colors
}

enum Value(val value: Int) {
    case One extends Value(1)
    case Two extends Value(2)
    case Three extends Value(3)
    case Four extends Value(4)
    case Five extends Value(5)
    case Six extends Value(6)
    case Seven extends Value(7)
    case Eight extends Value(8)
    // TODO complete the values
}

case class Card(suit: Suit, value: Value) {
    override def toString: String = s"$value of $suit"
}