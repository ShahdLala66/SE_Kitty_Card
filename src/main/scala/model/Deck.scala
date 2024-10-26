package model

import scala.util.Random

class Deck {
  private var cards: List[Card] = generateAndShuffleDeck()
  private var drawnCardsCount = 0
  private val maxCards = 69

  // Method to generate and shuffle the deck
  private def generateAndShuffleDeck(): List[Card] = {
    val newDeck = for {
      suit <- Suit.values.toList
      value <- Value1.values.toList
    } yield Card(suit, value)
    Random.shuffle(newDeck)
  }

  // Method to draw a card from the deck
  def drawCard(): Option[Card] = {
    if (cards.nonEmpty) {
      val card = cards.head
      cards = cards.tail
      drawnCardsCount += 1
      Some(card)
    } else if (drawnCardsCount < maxCards) {
      // Refill the deck automatically when it's empty and we haven't reached the limit
      cards = generateAndShuffleDeck()
      drawCard()  // Draw again after refilling
    } else {
      None  // Return None if we've reached the maximum number of draws
    }
  }

  // Method to refill the deck manually
  def refill(): Unit = {
    cards = generateAndShuffleDeck()
    drawnCardsCount = 0
  }

  // Method to get the size of the deck (number of remaining cards)
  def size: Int = cards.size
}
