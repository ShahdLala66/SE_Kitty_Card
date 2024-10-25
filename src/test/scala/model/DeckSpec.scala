import org.scalatest.funsuite.AnyFunSuite
import model.{Deck, Card, Suit, Value1}

class DeckSpec extends AnyFunSuite {

  test("Deck should draw a card and reduce deck size") {
    val deck = new Deck()
    val initialDeckSize = deck.size
    val drawnCard = deck.drawCard()
    assert(drawnCard.isDefined)  // A card is successfully drawn
    assert(deck.size == initialDeckSize - 1)  // Deck size should decrease by 1
  }

  test("Deck should allow repetitive cards") {
    val deck = new Deck()
    val card1 = deck.drawCard().get
    val card2 = deck.drawCard().get
    // It’s possible that card1 and card2 are the same (since the deck allows repetitive cards)
    assert(card1 != null)
    assert(card2 != null)
  }

  test("Deck should refill when empty") {
    val deck = new Deck()
    while (deck.size > 0) {
      deck.drawCard()
    }
    assert(deck.size == 0)  // Deck is empty now

    deck.refill()  // Refill the deck
    assert(deck.size > 0)  // Deck should have cards after refilling
  }
}