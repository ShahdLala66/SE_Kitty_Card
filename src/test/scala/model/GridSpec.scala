package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model.Suit._
import model.Value1._

class GridSpec extends AnyWordSpec with Matchers {

    "A Grid" should {

        "be initialized with the correct size and empty cells" in {
            val grid = Grid()
            grid.isFull() shouldBe false
            for {
                x <- 0 until 3
                y <- 0 until 3
            } yield grid.calculatePoints(x, y) shouldBe 0 // No cards initially, so points should be 0
        }



        "prevent placing a card out of bounds or in an occupied cell" in {
            val grid = Grid()
            val card = Card(Suit.Green, Value1.Eight)
            grid.placeCard(3, 3, card) shouldBe false // Out of bounds
            grid.placeCard(1, 1, card) shouldBe true
            grid.placeCard(1, 1, card) shouldBe false // Position already occupied
        }

        "calculate points correctly based on rectangle color" in {
            val grid = Grid()
            val cardBlue = Card(Suit.Blue, Value1.Five)
            val cardGreen = Card(Suit.Green, Value1.Three)

            // Place a blue card on a blue rectangle for double points
            grid.getRectangleColors(0)(0) = Suit.Blue
            grid.placeCard(0, 0, cardBlue)
            grid.calculatePoints(0, 0) shouldBe 10 // 5 * 2 for matching color

            // Place a green card on a white rectangle for normal points
            grid.getRectangleColors(1)(1) = Suit.White
            grid.placeCard(1, 1, cardGreen)
            grid.calculatePoints(1, 1) shouldBe 3
        }

        "return zero points if card suit does not match rectangle color" in {
            val grid = Grid()
            val cardRed = Card(Suit.Red, Value1.Two)

            grid.getRectangleColors(2)(2) = Suit.Blue // Mismatch
            grid.placeCard(2, 2, cardRed)
            grid.calculatePoints(2, 2) shouldBe 0
        }

        "recognize when the grid is full" in {
            val grid = Grid(2) // Smaller grid for easier testing
            val card = Card(Suit.Purple, Value1.One)

            for {
                x <- 0 until 2
                y <- 0 until 2
            } grid.placeCard(x, y, card)

            grid.isFull() shouldBe true
        }

        "display the initial colors of the rectangles" in {
            val grid = Grid()
            noException shouldBe thrownBy(grid.displayInitialColors()) // Check for correct display call
        }

        "display the grid with cards and rectangle colors correctly" in {
            val grid = Grid()
            val card = Card(Suit.Green, Value1.Eight)
            grid.placeCard(0, 0, card)
            noException shouldBe thrownBy(grid.display()) // Display without errors
        }
    }
}

