package model.baseImp

import model.{CardInterface, HandInterface}

class Hand extends HandInterface {
    private var cards: List[CardInterface] = List()

    def addCard(card: CardInterface): Unit = {
        cards = card :: cards
    }

    def getCards: List[CardInterface] = cards

    override def toString: String = cards.mkString(", ")
}