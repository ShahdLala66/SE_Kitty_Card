package model.handComp

import model.cardComp.Card

class Hand extends HandInterface {
    private var cards: List[Card] = List()

    def addCard(card: Card): Unit = {
        cards = card :: cards
    }

    def getCards: List[Card] = cards

    override def toString: String = cards.mkString(", ")
}