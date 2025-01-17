package model.baseImp

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridSpec extends AnyWordSpec with Matchers {

    "A Grid" should {

        "initialize with the correct size and empty cells" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)

            for (x <- 0 until size; y <- 0 until size) {
                grid.getCard(x, y)
                grid.getColor(x, y) shouldBe Suit.White
            }
        }

        "allow placing a card within bounds" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.White, Value.Two)

            grid.placeCard(1, 1, card)
            grid.getCard(1, 1) shouldBe Some(card)
        }

        "not allow placing a card out of bounds" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Green, Value.Two)

            grid.placeCard(-1, 1, card)
            grid.placeCard(1, -1, card)
            grid.placeCard(size, 1, card)
            grid.placeCard(1, size, card) shouldBe false
        }

        "allow removing a card" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Purple, Value.Two)

            grid.placeCard(1, 1, card)
            grid.removeCard(1, 1)
            grid.getCard(1, 1) shouldBe None
        }

        "calculate points correctly" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.Green)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Green, Value.Two)

            grid.placeCard(1, 1, card)
            grid.calculatePoints(1, 1) shouldBe 4
        }

        "undo the last move" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Red, Value.Two)

            grid.placeCard(1, 1, card)
            grid.undo()
            grid.getCard(1, 1) shouldBe None
        }
        "convert to an array of tuples correctly" in {
            val size = 3
            val rectangleColors = Array(
                Array(Suit.White, Suit.Red, Suit.Green),
                Array(Suit.Blue, Suit.Green, Suit.Purple),
                Array(Suit.White, Suit.White, Suit.Red)
            )
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Green, Value.Two)
            grid.placeCard(1, 1, card)

            val array = grid.toArray

            array.length
            array(1)(1)
            array(0)(0)
            array(2)(2) shouldBe(None, Suit.Red)
        }

        "correctly identify when it is full" in {
            val size = 2
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Green, Value.Two)

            grid.isFull

            for (x <- 0 until size; y <- 0 until size) {
                grid.placeCard(x, y, card)
            }

            grid.isFull shouldBe true
        }

        "correctly identify when it is not full" in {
            val size = 2
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.White, Value.Two)

            grid.placeCard(0, 0, card)
            grid.isFull shouldBe false
        }

        "return false when undo is called with no previous state" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)

            grid.undo() shouldBe false
        }

        "allow setting the color of a cell" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)

            grid.setColor(1, 1, Suit.Red)
            grid.getColor(1, 1) shouldBe Suit.Red
        }

        "update its cells and colors correctly from another grid" in {
            val size = 3
            val initialColors = Array.fill(size, size)(Suit.White)
            val newColors = Array(
                Array(Suit.Red, Suit.Green, Suit.Blue),
                Array(Suit.Green, Suit.Purple, Suit.White),
                Array(Suit.White, Suit.Red, Suit.Green)
            )
            val grid = Grid(size, initialColors)
            val newGrid = Grid(size, newColors)
            val card = NumberCards(Suit.Green, Value.Two)
            newGrid.placeCard(1, 1, card)
            grid.updateGrid(newGrid)

            for (x <- 0 until size; y <- 0 until size) {
                grid.getColor(x, y) shouldBe newColors(x)(y)
                grid.getCard(x, y)
            }
        }

        "calculate points correctly for a card on a white rectangle" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Brown, Value.Two)

            grid.placeCard(1, 1, card)
            grid.calculatePoints(1, 1) shouldBe 2
        }

        "calculate points correctly for a card on a different colored rectangle" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.Red)
            val grid = Grid(size, rectangleColors)
            val card = NumberCards(Suit.Brown, Value.Two)

            grid.placeCard(1, 1, card)
            grid.calculatePoints(1, 1) shouldBe 0
        }

        "calculate points correctly when there is no card" in {
            val size = 3
            val rectangleColors = Array.fill(size, size)(Suit.White)
            val grid = Grid(size, rectangleColors)

            grid.calculatePoints(1, 1) shouldBe 0
        }

    }
}