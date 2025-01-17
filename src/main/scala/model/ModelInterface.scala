package model

import model.baseImp.Suit.Suit

trait CardInterface{
    def getColor: String
    def toString: String
}

trait DeckInterface {
    def drawCard(): Option[CardInterface]
    def size: Int
}

trait HandInterface {
    def addCard(card: CardInterface): Unit
    def getCards: List[CardInterface]
    def toString: String
}

trait PlayerInterface(nameI: String, var pointsI: Int = 0) {
    def getPlayerName: String = nameI
    def getPoints: Int = pointsI
    def hand: List[CardInterface]
    def addPoints(newPoints: Int): Unit
    def setPoints(newPoints: Int): Unit
    def drawCard(deck: DeckInterface): Option[CardInterface]
    def updatePoints(newPoints: Int): Unit
    def getHand: List[CardInterface]
    def updateHand(newHand: List[CardInterface]): Unit
    def removeCard(card: CardInterface): Unit
    def setHand(newHand: List[CardInterface]): Unit
}