package model.Objects

import model.cards.{NumberCards, Suit, Value}

import scala.util.Random

class Deck {
  private val cards: List[NumberCards] = for {
    suit <- Suit.values.toList if suit != Suit.White // Exclude White suit
    value <- Value.values.toList
  } yield model.cards.NumberCards(suit, value)

  private var shuffledDeck: List[NumberCards] = Random.shuffle(cards)

  def drawCard(): Option[NumberCards] = {
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