package model

import model.Suit._
import model.Value1._

case class Grid(size: Int = 3) {
    val eol: String = sys.props("line.separator") // End of line character

    // Initialize an empty grid with None values, representing empty spots
    private var grid: Array[Array[Option[Card]]] = Array.fill(size, size)(None)

    // Helper method to check if a position is within the grid bounds
    private def isWithinBounds(x: Int, y: Int): Boolean = {
        x >= 0 && x < size && y >= 0 && y < size
    }

    // Method to place a card on the grid
    def placeCard(x: Int, y: Int, card: Card, rectangleColor: Suit): Boolean = {
        if (isWithinBounds(x, y) && grid(x)(y).isEmpty) {
            grid(x)(y) = Some(card)
            true
        } else {
            false  // Return false if the position is out of bounds or already occupied
        }
    }

    // Method to calculate points for a given position
    def calculatePoints(x: Int, y: Int, rectangleColor: Suit): Int = {
        grid(x)(y) match {
            case Some(card) =>
                if (card.suit == rectangleColor) {
                    Value1.toInt(card.value) * 2  // Double the value if the suit matches
                } else if (rectangleColor == Suit.White) {
                    Value1.toInt(card.value)  // Keep the value if placed on a white rectangle
                } else {
                    0  // No points if the suit doesn't match
                }
            case None => 0  // No card means no points
        }
    }

    // Method to display the grid
    def display(): Unit = {
        for (row <- grid) {
            println(row.map {
                case Some(card) => s"${card.value} of ${card.suit}"
                case None       => "Empty"
            }.mkString(" | "))
        }
    }

    // Method to return a string representation of the grid
    def mesh(): String = {
        grid.map { row =>
            row.map {
                case Some(card) => s"${card.value} of ${card.suit}"
                case None       => "Empty"
            }.mkString(" | ")
        }.mkString(eol)
    }
}