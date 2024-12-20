package model.gridComp

import model.cardComp.Suit.Suit
import model.cardComp.NumberCards

trait GridInterface {
    def size: Int
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
}