import org.scalatest.funsuite.AnyFunSuite
import model.{Card, Suit, Value1}

class CardTest extends AnyFunSuite {

  test("Card should correctly display its suit and value") {
    val card = Card(Suit.Blue, Value1.Five)
    assert(card.toString == "Five of Blue")
  }

  test("Card should hold the correct suit and value") {
    val card = Card(Suit.Green, Value1.Seven)
    assert(card.suit == Suit.Green)
    assert(card.value == Value1.Seven)
  }
}
