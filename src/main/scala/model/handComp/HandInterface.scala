package model.handComp

import model.cardComp.CardInterface

trait HandInterface {
    def addCard(card: CardInterface): Unit
    def getCards: List[CardInterface]
    def toString: String
}