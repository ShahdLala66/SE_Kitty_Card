package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec with Matchers {

  "Card" should {
    "Card should correctly display its suit and value" in {
      val card = Card(Suit.Blue, Value1.Five)
      card.toString shouldBe "Five of Blue"
    }

  }

}
