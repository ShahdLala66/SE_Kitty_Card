package model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CardSpec extends AnyWordSpec with Matchers {

  "A Card" should {

    "correctly display its suit and value" in {
      val card = Card(Suit.Blue, Value1.Five)
      card.toString shouldEqual "Five of Blue"
    }

    "hold the correct suit and value" in {
      val card = Card(Suit.Green, Value1.Seven)
      card.suit shouldEqual Suit.Green
      card.value shouldEqual Value1.Seven
    }
  }
}