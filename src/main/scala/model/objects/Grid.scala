package model.objects

import model.objects.cards.Suit.*
import model.objects.cards.Value.*
import model.objects.cards.{NumberCards, Suit, Value}

import scala.util.Random

case class Grid(size: Int) {
    val eol: String = sys.props("line.separator") // End of line character

    // Initialize an empty grid with None values, representing empty spots
    private val grid: Array[Array[Option[NumberCards]]] = Array.fill(size, size)(None)
    private val rectangleColors: Array[Array[Suit]] = generateRandomRectangles()
    private val catPrint = new aview.Tui() // Create CatPrint instance

    // List to store the history of grid states for undo functionality
    private var history: List[Array[Array[Option[NumberCards]]]] = List(grid.map(_.clone))

    // Hilfsmethode für Testzwecke, um auf die Farben der Rechtecke zuzugreifen
    def getRectangleColors(x: Int, y: Int): Suit = rectangleColors(x)(y)

    def getCard(x: Int, y: Int): Option[NumberCards] = grid(x)(y)

    def getColor(x: Int, y: Int): Suit = rectangleColors(x)(y)

    def setColor(x: Int, y: Int, color: Suit): Unit = {
        rectangleColors(x)(y) = color
    }

    def removeCard(x: Int, y: Int): Unit = {
        grid(x)(y) = None
    }

    // Generate up to 4 random colored rectangles, others default to White
    private def generateRandomRectangles(): Array[Array[Suit]] = {
        val colors = List(Suit.Blue, Suit.Green, Suit.Purple, Suit.Red)
        val positions = Random.shuffle(for {
            x <- 0 until size
            y <- 0 until size
        } yield (x, y)).take(4)

        val colorGrid = Array.fill(size, size)(Suit.White) // Start with all White
        positions.zip(Random.shuffle(colors)).foreach { case ((x, y), color) =>
            colorGrid(x)(y) = color
        }

        colorGrid
    }

    // Helper method to check if a position is within the grid bounds
    private def isWithinBounds(x: Int, y: Int): Boolean = {
        x >= 0 && x < size && y >= 0 && y < size
    }

    // Method to place a card on the grid
    def placeCard(x: Int, y: Int, card: NumberCards): Boolean = {
        if (isWithinBounds(x, y) && grid(x)(y).isEmpty) {
            saveState() // Save the current state before making a change
            grid(x)(y) = Some(card)
            true
        } else {
            false // Return false if the position is out of bounds or already occupied
        }
    }

    // Method to calculate points for a given position
    def calculatePoints(x: Int, y: Int): Int = {
        val rectangleColor = rectangleColors(x)(y)
        grid(x)(y) match {
            case Some(card) =>
                println(s"Card: ${card.suit}, ${card.value}; Rectangle color: $rectangleColor")
                if (card.suit == rectangleColor) {
                    Value.toInt(card.value) * 2 // Double points if suits match
                } else if (rectangleColor == Suit.White) {
                    Value.toInt(card.value) // Regular points if placed on a white rectangle
                } else {
                    0 // No points if placed on a different colored rectangle
                }
            case None => 0 // No card means no points
        }
    }

    // Check if the grid is full (all cells contain cards)
    def isFull: Boolean = {
        grid.flatten.forall(_.isDefined)
    }

    def updateGrid(newGrid: Grid): Unit = {
        for (i <- grid.indices; j <- grid(i).indices) {
            grid(i)(j) = newGrid.grid(i)(j)
            rectangleColors(i)(j) = newGrid.rectangleColors(i)(j)
        }
    }

    // Method to display the grid along with rectangle colors
    def display(): Unit = {
        println("Grid layout (cards and rectangle colors):")
        for (i <- grid.indices) {
            for (j <- grid(i).indices) {
                val cardDisplay = grid(i)(j) match {
                    case Some(card) => s"${card.suit}, ${card.value}"
                    case None => "Empty"
                }
                val colorDisplay = s"[${rectangleColors(i)(j)}]"
                print(f"$cardDisplay%-20s $colorDisplay%-10s | ")
            }
            println()
        }
    }

    // Show only rectangle colors (before any cards are placed)
    def displayInitialColors(): Unit = {
        println("Initial grid layout with rectangle colors:")
        for (i <- rectangleColors.indices) {
            for (j <- rectangleColors(i).indices) {
                val colorDisplay = s"[${rectangleColors(i)(j)}]"
                print(f"Empty                $colorDisplay%-10s | ")
            }
            println()
        }
    }

    def setRectangleColor(x: Int, y: Int, color: Suit): Unit = {
        rectangleColors(x)(y) = color
    }

    // Save the current state of the grid to the history
    private def saveState(): Unit = {
        val gridCopy = grid.map(_.map(_.map(card => card.copy())))
        history = gridCopy :: history
    }

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
}

object Grid {
    private var instance: Option[Grid] = None // Singleton instance

    def getInstance(size: Int): Grid = {
        if (instance.isEmpty) {
            instance = Some(new Grid(size))
        }
        instance.get
    }
}