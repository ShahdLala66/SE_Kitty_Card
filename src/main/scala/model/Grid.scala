package model

import scala.util.Random
import model.cards.Suit.*
import model.cards.Value.*
import model.cards.{NumberCards, Suit, Value}

case class Grid(size: Int = 3) {
    val eol: String = sys.props("line.separator") // End of line character

    // Initialize an empty grid with None values, representing empty spots
    private val grid: Array[Array[Option[NumberCards]]] = Array.fill(size, size)(None)
    private val rectangleColors: Array[Array[Suit]] = generateRandomRectangles()
    private val catPrint = new view.Tui() // Create CatPrint instance

    // Hilfsmethode für Testzwecke, um auf die Farben der Rechtecke zuzugreifen
    def getRectangleColors(x: Int, y: Int): Suit = rectangleColors(x)(y)

    // Generate up to 4 random colored rectangles, others default to White
    private def generateRandomRectangles(): Array[Array[Suit]] = {
        val colors = List(Suit.Blue, Suit.Green, Suit.Purple, Suit.Red)
        val positions = Random.shuffle(for {
            x <- 0 until size
            y <- 0 until size
        } yield (x, y)).take(4)

        val colorGrid = Array.fill(size, size)(Suit.White)  // Start with all White
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
                    Value.toInt(card.value) * 2  // Double points if suits match
                } else if (rectangleColor == Suit.White) {
                    Value.toInt(card.value)  // Regular points if placed on a white rectangle
                } else {
                    0  // No points if placed on a different colored rectangle
                }
            case None => 0  // No card means no points
        }
    }

    // Check if the grid is full (all cells contain cards)
    def isFull: Boolean = {
        grid.flatten.forall(_.isDefined)
    }

    // Method to display the grid along with rectangle colors
    def display(): Unit = {
        println("Grid layout (cards and rectangle colors):")
        for (i <- grid.indices) {
            for (j <- grid(i).indices) {
                val cardDisplay = grid(i)(j) match {
                    case Some(card) => s"${card.suit}, ${card.value}"
                    case None       => "Empty"
                }
                val colorDisplay = s"[${rectangleColors(i)(j)}]"
                print(f"$cardDisplay%-12s $colorDisplay%-10s | ")
            }
            println()
        }
    }

    // Show only rectangle colors (before any cards are placed)
    def displayInitialColors(): Unit = {
        println("Grid layout with initial rectangle colors:")
        for (i <- rectangleColors.indices) {
            for (j <- rectangleColors(i).indices) {
                print(f"${rectangleColors(i)(j)}%-10s | ")
            }
            println()
        }
    }

    def setRectangleColor(x: Int, y: Int, color: Suit): Unit = {
        rectangleColors(x)(y) = color
    }

}