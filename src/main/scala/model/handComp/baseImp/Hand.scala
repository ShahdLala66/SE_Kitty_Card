// src/main/scala/model/cards/Hand.scala
package model.handComp.baseImp

import model.cardComp.CardInterface
import model.handComp.HandInterface

class Hand extends HandInterface {
    private var cards: List[CardInterface] = List()

    def addCard(card: CardInterface): Unit = {
        cards = card :: cards
    }

    def getCards: List[CardInterface] = cards

    override def toString: String = cards.mkString(", ")
}