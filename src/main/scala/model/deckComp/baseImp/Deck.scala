package model.deckComp.baseImp

import model.cardComp.baseImp.{NumberCards, Suit, Value}
import model.cardComp.{CardInterface, baseImp}
import model.deckComp.DeckInterface

import scala.util.Random

class Deck extends DeckInterface {
    private val cards: List[CardInterface] = for {
        suit <- Suit.values.toList if suit != Suit.White // Exclude White suit
        value <- Value.values.toList
    } yield baseImp.NumberCards(suit, value)

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