package model

import model.cards.Suit.*
import model.cards.Value.*
import model.cards.{NumberCards, Suit, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridSpec extends AnyWordSpec with Matchers {

    "A Grid" should {

        "have a default size of 3" in {
            val grid = Grid()
            grid.size shouldBe 3
        }

        "initialize with empty spots" in {
            val grid = Grid()
            for (i <- 0 until grid.size; j <- 0 until grid.size) {
                val result = grid.placeCard(i, j, NumberCards(Suit.Red, Value.Two))
            }
        }

        "place a card on the grid" in {
            val grid = Grid()
            val card = NumberCards(Suit.Red, Value.Two)
            grid.placeCard(0, 0, card)
            grid.placeCard(0, 0, card) shouldBe false // Position already occupied
        }

        "calculate points correctly" in {
            val grid = Grid()
            val card = NumberCards(Suit.Red, Value.Two)
            grid.setRectangleColor(0, 0, Suit.Red)
            grid.placeCard(0, 0, card)
            grid.calculatePoints(0, 0) shouldBe 4 // Double points for matching suit
        }

        "check if the grid is full" in {
            val grid = Grid()
            for (i <- 0 until grid.size; j <- 0 until grid.size) {
                grid.placeCard(i, j, NumberCards(Suit.Red, Value.Two))
            }
            grid.isFull() shouldBe true
        }


        "generate a grid with up to 4 non-white rectangles" in {
            val grid = Grid()
            val nonWhiteCount = (for {
                x <- 0 until 3
                y <- 0 until 3
                if grid.getRectangleColors(x, y) != Suit.White
            } yield 1).sum
            nonWhiteCount should (be >= 0 and be <= 4)
        }

        "return zero points when no card is placed at the given position" in {
            val grid = Grid()
            grid.calculatePoints(0, 0) shouldBe 0
        }

        "allow placing a card in an empty spot within bounds" in {
            val grid = Grid()
            val card = NumberCards(Suit.Green, One)
            grid.placeCard(0, 0, card) shouldBe true
        }

        "not allow placing a card in an occupied spot" in {
            val grid = Grid()
            val card1 = cards.NumberCards(Suit.Green, One)
            val card2 = cards.NumberCards(Suit.Blue, Two)
            grid.placeCard(0, 0, card1)
            grid.placeCard(0, 0, card2) shouldBe false
        }

        "not allow placing a card out of bounds" in {
            val grid = Grid()
            val card = cards.NumberCards(Suit.Green, One)
            grid.placeCard(-1, 0, card)
            grid.placeCard(3, 3, card) shouldBe false
        }

        "calculate points correctly when card matches rectangle color" in {
            val grid = Grid()
            val card = cards.NumberCards(Suit.Blue, Three)
            grid.placeCard(0, 0, card)
            grid.setRectangleColor(0, 0, Suit.Blue) // Use setter method
            grid.calculatePoints(0, 0) shouldBe 6 // 3 * 2 = 6
        }

        "calculate points correctly when placed on a white rectangle" in {
            val grid = Grid()
            val card = cards.NumberCards(Suit.Blue, Three)
            grid.placeCard(1, 1, card)
            grid.setRectangleColor(1, 1, Suit.White) // Use setter method
            grid.calculatePoints(1, 1) shouldBe 3 // no double points, should be 3
        }

        "award zero points when placed on a different colored rectangle" in {
            val grid = Grid()
            val card = cards.NumberCards(Suit.Blue, Three)
            grid.placeCard(2, 2, card)
            grid.setRectangleColor(2, 2, Suit.Red) // Use setter method
            grid.calculatePoints(2, 2) shouldBe 0
        }


        "correctly identify a full grid" in {
            val grid = Grid(2) // smaller grid for testing
            for (x <- 0 until 2; y <- 0 until 2) {
                grid.placeCard(x, y, cards.NumberCards(Suit.Blue, One))
            }
            grid.isFull() shouldBe true
        }

        "correctly identify a non-full grid" in {
            val grid = Grid()
            grid.placeCard(0, 0, cards.NumberCards(Suit.Green, One))
            grid.isFull() shouldBe false
        }

        "display the initial colors of the grid correctly" in {
            val grid = Grid()
            noException should be thrownBy grid.displayInitialColors()
        }

        "display the grid with cards and colors correctly" in {
            val grid = Grid()
            grid.placeCard(0, 0, cards.NumberCards(Suit.Blue, Two))
            noException should be thrownBy grid.display()
        }
    }
}
