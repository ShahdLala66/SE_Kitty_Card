package model.cards

import model.cards.Suit
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class SuitSpec extends AnyWordSpec with Matchers {
    "A Suit" should {
        "contain six values" in {
            Suit.values.size shouldBe 6
        }

        "contain the correct values" in {
            val expectedValues = Set(Suit.Green, Suit.Brown, Suit.Purple, Suit.Blue, Suit.Red, Suit.White)
            Suit.values shouldBe expectedValues
        }

        "have a value named Green" in {
            Suit.withName("Green") shouldBe Suit.Green
        }

        "have a value named Brown" in {
            Suit.withName("Brown") shouldBe Suit.Brown
        }

        "have a value named Purple" in {
            Suit.withName("Purple") shouldBe Suit.Purple
        }

        "have a value named Blue" in {
            Suit.withName("Blue") shouldBe Suit.Blue
        }

        "have a value named Red" in {
            Suit.withName("Red") shouldBe Suit.Red
        }

        "have a value named White" in {
            Suit.withName("White") shouldBe Suit.White
        }
    }
}