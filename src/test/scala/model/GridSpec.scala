// src/test/scala/model/GridSpec.scala
package model

import model.objects.Grid
import model.objects.cards.{NumberCards, Suit, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridSpec extends AnyWordSpec with Matchers {

    "A Grid" should {

        "initialize with the correct size" in {
            val grid = Grid.getInstance(3)
            grid.size shouldBe 3
        }

        "place a card correctly" in {
            val grid = Grid.getInstance(4)
            val card = NumberCards(Suit.Blue, Value.Two)
            grid.placeCard(0, 0, card) shouldBe true // Position already occupied
        }

        "calculate points correctly" in {
            val grid = Grid.getInstance(4)
            val card = NumberCards(Suit.Blue, Value.Two)
            grid.setRectangleColor(0, 0, Suit.Blue)
            grid.placeCard(0, 0, card)
            grid.calculatePoints(0, 0) shouldBe 4 // Double points for matching suit
        }

        "detect if the grid is full" in {
            val grid = Grid.getInstance(4)
            val card = NumberCards(Suit.Blue, Value.Two)
            for (x <- 0 until 4; y <- 0 until 4) {
                grid.placeCard(x, y, card)
            }
            grid.isFull shouldBe true
        }

        "display the initial rectangle colors" in {
            val grid = Grid.getInstance(4)
            grid.displayInitialColors() // This will print the initial colors
        }

        "display the grid with cards and rectangle colors" in {
            val grid = Grid.getInstance(4)
            val card = NumberCards(Suit.Blue, Value.Two)
            grid.placeCard(0, 0, card)
            grid.placeCard(1, 1, card)
            grid.display() // This will print the grid with cards and rectangle colors
        }

        "calculate points correctly for a card placed on a white rectangle" in {
            val grid = Grid.getInstance(4)
            val card = NumberCards(Suit.Blue, Value.Two)
            grid.setRectangleColor(0, 0, Suit.White)
            grid.placeCard(0, 0, card)
            grid.calculatePoints(0, 0) shouldBe 2 // Regular points for white rectangle
        }

        "calculate points correctly for a card placed on a different colored rectangle" in {
            val grid = Grid.getInstance(4)
            val card = NumberCards(Suit.Blue, Value.Two)
            grid.setRectangleColor(0, 0, Suit.Red)
            grid.placeCard(0, 0, card)
            grid.calculatePoints(0, 0) shouldBe 0 // No points for different colored rectangle
        }

        "return the correct rectangle color" in {
            val grid = Grid.getInstance(4)
            grid.setRectangleColor(0, 0, Suit.Blue)
            grid.getRectangleColors(0, 0) shouldBe Suit.Blue
        }

        "return 0 points if no card is placed" in {
            val grid = Grid.getInstance(4)
            grid.calculatePoints(0, 0) shouldBe 4 // No card means no points
        }

        "allow placing a card in an empty spot within bounds" in {
            val grid = Grid.getInstance(4)
            val card = NumberCards(Suit.Green, Value.Four)
            grid.placeCard(0, 0, card) shouldBe false
        }
    }
}