package model.deckComp

import model.cardComp.{NumberCards, Suit, Value}

import scala.util.Random

class Deck extends DeckInterface {
    private val cards: List[NumberCards] = for {
        suit <- Suit.values.toList if suit != Suit.White // Exclude White suit
        value <- Value.values.toList
    } yield model.cardComp.NumberCards(suit, value)

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