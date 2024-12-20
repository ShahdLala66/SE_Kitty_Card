package model.handComp

import model.cardComp.baseImp.Card

trait HandInterface {
    def addCard(card: Card): Unit
    def getCards: List[Card]
    def toString: String
}