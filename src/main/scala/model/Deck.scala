package model

import scala.util.Random

class Deck {
  private val cards: List[Card] = for {
    suit <- Suit.values.toList
    value <- Value1.values.toList
  } yield Card(suit, value)

  private var shuffledDeck: List[Card] = Random.shuffle(cards)

  def drawCard(): Option[Card] = {
    if (shuffledDeck.nonEmpty) {
      val card = shuffledDeck.head
      shuffledDeck = shuffledDeck.tail
      Some(card)
    } else {
      None
    }
  }

  def size: Int = shuffledDeck.size
}