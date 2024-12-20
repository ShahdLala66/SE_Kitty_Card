package model

import model.cardComp.Suit.Suit
import model.cardComp.{Card, NumberCards}
import model.deckComp.Deck
import model.gridComp.Grid

trait CompInterface {

    // Player
    def name: String
    def points: Int
    def addPoints(newPoints: Int): Unit
    def setPoints(newPoints: Int): Unit
    def drawCard(deck: Deck): Option[Card]
    def updatePoints(newPoints: Int): Unit
    def getHand: List[Card]
    def updateHand(newHand: List[Card]): Unit
    def removeCard(card: Card): Unit

    // Hand
    def addCard(card: Card): Unit
    def getCards: List[Card]
    def toString: String

    // Deck
    def drawCard(): Option[NumberCards]
    def size: Int

    // Grid
    def rectangleColors: Array[Array[Suit]]
    def toArray: Array[Array[(Option[NumberCards], Suit)]]
    def getCard(x: Int, y: Int): Option[NumberCards]
    def getColor(x: Int, y: Int): Suit
    def isFull: Boolean
    def undo(): Boolean
    def placeCard(x: Int, y: Int, card: NumberCards): Boolean
    def isWithinBounds(x: Int, y: Int): Boolean
    def saveState(): Unit
    def setColor(x: Int, y: Int, color: Suit): Unit
    def removeCard(x: Int, y: Int): Unit
    def updateGrid(newGrid: Grid): Unit
    def calculatePoints(x: Int, y: Int): Int

    // Card
    def getColor: String

}
