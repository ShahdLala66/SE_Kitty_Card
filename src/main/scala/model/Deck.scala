package model

import scala.util.Random

class Deck {
  private var cards: List[Card] = generateDeck()

  // Method to generate a full deck of cards
  private def generateDeck(): List[Card] = {
    for {
      suit <- Suit.values.toList
      value <- Value1.values.toList
    } yield Card(suit, value)
  }

  // Method to draw a card from the deck
  def drawCard(): Option[Card] = {
    if (cards.nonEmpty) {
      val card = cards.head
      cards = cards.tail
      Some(card)
    } else {
      None
    }
  }

  // Method to refill the deck
  def refill(): Unit = {
    cards = generateDeck()
    Random.shuffle(cards)
  }

  // Method to get the size of the deck (number of remaining cards)
  def size: Int = cards.size
}
