// src/main/scala/model/cards/Hand.scala
package model.cards

class Hand {
    private var cards: List[Card] = List()

    def addCard(card: Card): Unit = {
        cards = card :: cards
    }

    def getCards: List[Card] = cards

    override def toString: String = cards.mkString(", ")
}