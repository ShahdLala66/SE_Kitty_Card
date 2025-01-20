package model.gameModelComp.baseImp

import Suit.Suit
import util.Observer

import scala.collection.mutable.ListBuffer

case class Grid(size: Int, rectangleColors: Array[Array[Suit]]) {
    private val observers = ListBuffer[Observer]()
    private val grid: Array[Array[Option[NumberCards]]] = Array.fill(size, size)(None)

    def toArray: Array[Array[(Option[NumberCards], Suit)]] = {
        Array.tabulate(size, size) { (x, y) =>
            (grid(x)(y), rectangleColors(x)(y))
        }
    }

    private var history: List[Array[Array[Option[NumberCards]]]] = List(grid.map(_.clone))

    def getCard(x: Int, y: Int): Option[NumberCards] = grid(x)(y)

    def getColor(x: Int, y: Int): Suit = rectangleColors(x)(y)

    def isFull: Boolean = grid.flatten.forall(_.isDefined)

    def undo(): Boolean = {
        history match {
            case _ :: previousState :: rest =>
                for (i <- grid.indices; j <- grid(i).indices) {
                    grid(i)(j) = previousState(i)(j)
                }
                history = rest
                true
            case _ => false
        }
    }

    def placeCard(x: Int, y: Int, card: NumberCards): Boolean = {
        if (isWithinBounds(x, y) && grid(x)(y).isEmpty) {
            saveState() 
            grid(x)(y) = Some(card)
            true
        } else {
            false
        }
    }

    private def isWithinBounds(x: Int, y: Int): Boolean = x >= 0 && x < size && y >= 0 && y < size

    private def saveState(): Unit = {
        val gridCopy = grid.map(_.map(_.map(card => card.copy())))
        history = gridCopy :: history
    }

    def setColor(x: Int, y: Int, color: Suit): Unit = {
        rectangleColors(x)(y) = color
    }

    def removeCard(x: Int, y: Int): Unit = {
        grid(x)(y) = None
    }

    def updateGrid(newGrid: Grid): Unit = {
        for (i <- grid.indices; j <- grid(i).indices) {
            grid(i)(j) = newGrid.grid(i)(j)
            rectangleColors(i)(j) = newGrid.rectangleColors(i)(j)
        }
    }

    def calculatePoints(x: Int, y: Int): Int = {
        val rectangleColor = rectangleColors(x)(y)
        grid(x)(y) match {
            case Some(card) =>
                if (card.suit == rectangleColor) {
                    Value.toInt(card.value) * 2
                } else if (rectangleColor == Suit.White) {
                    Value.toInt(card.value)
                } else {
                    0
                }
            case None => 0
        }
    }

}