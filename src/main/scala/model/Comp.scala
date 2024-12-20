package model

import model.cardComp.*
import model.cardComp.Suit.Suit
import model.deckComp.*
import model.gridComp.*
import model.handComp.*
import model.playerComp.*

case class Comp(card: CardInterface, deck: DeckInterface, grid: GridInterface,
                handI: HandInterface, player: PlayerInterface) extends CompInterface {

    // Player
    def name: String = {
        player.name
    }

    def points: Int = {
        player.points
    }

    def addPoints(newPoints: Int): Unit = {
        player.addPoints(newPoints)
    }

    def setPoints(newPoints: Int): Unit = {
        player.setPoints(newPoints)
    }

    def drawCard(deck: Deck): Option[Card] = {
        player.drawCard(deck)
    }

    def updatePoints(newPoints: Int): Unit = {
        player.updatePoints(newPoints)
    }

    def getHand: List[Card] = {
        player.getHand
    }

    def updateHand(newHand: List[Card]): Unit = {
        player.updateHand(newHand)
    }

    def removeCard(card: Card): Unit = {
        player.removeCard(card)
    }

    // Hand
    def addCard(card: Card): Unit = {
        handI.addCard(card)
    }

    def getCards: List[Card] = {
        handI.getCards
    }

    override def toString: String = {
        handI.toString
    }

    // Deck
    def drawCard(): Option[NumberCards] = {
        deck.drawCard()
    }

    def size: Int = {
        deck.size
    }

    // Grid
    def rectangleColors: Array[Array[Suit]] = {
        grid.rectangleColors
    }

    def toArray: Array[Array[(Option[NumberCards], Suit)]] = {
        grid.toArray
    }

    def getCard(x: Int, y: Int): Option[NumberCards] = {
        grid.getCard(x, y)
    }

    def getColor(x: Int, y: Int): Suit = {
        grid.getColor(x, y)
    }

    def isFull: Boolean = {
        grid.isFull
    }

    def undo(): Boolean = {
        grid.undo()
    }

    def placeCard(x: Int, y: Int, card: NumberCards): Boolean = {
        grid.placeCard(x, y, card)
    }

    def isWithinBounds(x: Int, y: Int): Boolean = {
        grid.isWithinBounds(x, y)
    }

    def saveState(): Unit = {
        grid.saveState()
    }

    def setColor(x: Int, y: Int, color: Suit): Unit = {
        grid.setColor(x, y, color)
    }

    def removeCard(x: Int, y: Int): Unit = {
        grid.removeCard(x, y)
    }

    def updateGrid(newGrid: Grid): Unit = {
        grid.updateGrid(newGrid)
    }

    def calculatePoints(x: Int, y: Int): Int = {
        grid.calculatePoints(x, y)
    }

    // Card
    def getColor: String = {
        card.getColor
    }

    def this() = this(NumberCards(Suit.Green, Value.Three), new Deck, Grid(3, Array.fill(3, 3)(Suit.White)), new Hand, Player("Default"))
}