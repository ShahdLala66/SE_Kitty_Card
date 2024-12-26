package model.playerComp

import model.cardComp.CardInterface
import model.deckComp.DeckInterface

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
}
