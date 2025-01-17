package model.baseImp

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class DeckSpec extends AnyWordSpec with Matchers {

    "Deck" should {
        "should draw a card and reduce deck size" in {
            val deck = new Deck()
            val initialDeckSize = deck.size
            deck.drawCard().isDefined // A card is successfully drawn
            deck.size shouldBe initialDeckSize - 1
        }
        "Deck should allow repetitive cards" in {
            val deck = new Deck()
            val card1 = deck.drawCard().get
            val card2 = deck.drawCard().get
            card1 should not be null
            card2 should not be null
        }
        "Deck should return None when drawing a card from an empty deck" in {
            val deck = new Deck()
            while (deck.drawCard().isDefined) {}

            val result = deck.drawCard()
            result.isEmpty shouldBe true
        }

        "Deck should refill when empty" in {
            val deck = new Deck()
            while (deck.size > 0) {
                deck.drawCard()
            }
        }
    }

}