package model

import model.Suit.Suit
import model.Value1.Value

case class Card(suit: Suit.Value, value: Value1.Value) {
    override def toString: String = s"${value.toString} of ${suit.toString}"
}
