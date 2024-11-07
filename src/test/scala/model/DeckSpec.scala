package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class DeckSpec extends AnyWordSpec with Matchers {

  "Dick" should {
    "should draw a card and reduce deck size" in {
      val deck = new Deck()
      val initialDeckSize = deck.size
      deck.drawCard().isDefined shouldBe true // A card is successfully drawn
      deck.size shouldBe initialDeckSize - 1
    }
    "Deck should allow repetitive cards" in {
      val deck = new Deck()
      val card1 = deck.drawCard().get
      val card2 = deck.drawCard().get
      // It’s possible that card1 and card2 are the same (since the deck allows repetitive cards)
      card1 should not be null
      card2 should not be null
    }
    "Deck should return None when drawing a card from an empty deck" in {
      val deck = new Deck()
      // Draw all cards to empty the deck
      while (deck.drawCard().isDefined) {}

      // Now the deck should be empty
      val result = deck.drawCard()
      result.isEmpty shouldBe true 
    }

    "Deck should refill when empty" in {
      val deck = new Deck()
      while (deck.size > 0) {
        deck.drawCard()
      }
      
      deck.size should equal (0)    // Deck is empty now
      deck.refill() // Refill the deck
      deck.size should be > 0   // Deck should have cards after refilling
    }
  }

}
