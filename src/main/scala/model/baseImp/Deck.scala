package model.baseImp

import model.*

import scala.util.Random

class Deck extends DeckInterface {
    private val cards: List[CardInterface] = for {
        suit <- Suit.values.toList if suit != Suit.White // Exclude White suit
        value <- Value.values.toList
    } yield NumberCards(suit, value)

    private var shuffledDeck: List[CardInterface] = Random.shuffle(cards)

    def drawCard(): Option[CardInterface] = {
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