package model.handComp

import model.cardComp.Card

trait HandInterface {
    def addCard(card: Card): Unit
    def getCards: List[Card]
    def toString: String
}