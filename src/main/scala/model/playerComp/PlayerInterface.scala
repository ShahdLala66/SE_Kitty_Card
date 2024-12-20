package model.playerComp

import model.cardComp.baseImp.Card
import model.deckComp.baseImp.Deck

trait PlayerInterface {
    def name: String
    def points: Int
    def hand: List[Card]
    def addPoints(newPoints: Int): Unit
    def setPoints(newPoints: Int): Unit
    def drawCard(deck: Deck): Option[Card]
    def updatePoints(newPoints: Int): Unit
    def getHand: List[Card]
    def updateHand(newHand: List[Card]): Unit
    def removeCard(card: Card): Unit
}