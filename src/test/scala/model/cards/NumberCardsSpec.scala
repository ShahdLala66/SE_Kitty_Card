package model.cards

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class NumberCardsSpec extends AnyWordSpec with Matchers {

  "Card" should {
    "Card should correctly display its suit and value" in {
      val card = NumberCards(Suit.Blue, Value.Five)
      card.toString shouldBe "Five of Blue"
    }

  }

}
