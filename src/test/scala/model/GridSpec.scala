package model

import model.Suit._
import model.Value1._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridSpec extends AnyWordSpec with Matchers {

  private def createGridWithCard(x: Int, y: Int, card: Card, rectangleColor: Suit): Grid = {
    val grid = new Grid()
    grid.placeCard(x, y, card, rectangleColor)
    grid
  }

  "A Grid" should {

    "place a card successfully when position is valid and empty" in {
      val grid = new Grid()
      val card = Card(Suit.Red, Value1.One)
      val result = grid.placeCard(0, 0, card, Suit.White)
      result should be(true)
      grid.calculatePoints(0, 0, Suit.Red) should be(2) // 1 * 2
    }

    "return false when position is out of bounds" in {
      val grid = new Grid()
      val card = Card(Suit.Red, Value1.One)
      val result = grid.placeCard(-1, 0, card, Suit.White)
      result should be(false)
    }

    "return false when position is already occupied" in {
      val grid = new Grid()
      val card1 = Card(Suit.Red, Value1.One)
      val card2 = Card(Suit.Green, Value1.Two)
      grid.placeCard(0, 0, card1, Suit.White)
      val result = grid.placeCard(0, 0, card2, Suit.White)
      result should be(false)
    }

    "return 0 points for an empty position" in {
      val grid = new Grid()
      val points = grid.calculatePoints(0, 0, Suit.Red)
      points should be(0)
    }

    "double the value if suit matches rectangle color" in {
      val grid = createGridWithCard(0, 0, Card(Suit.Red, Value1.Two), Suit.Red)
      val points = grid.calculatePoints(0, 0, Suit.Red)
      points should be(4) // 2 * 2
    }

    "return the value if rectangle color is white" in {
      val grid = createGridWithCard(0, 0, Card(Suit.Red, Value1.Two), Suit.White)
      val points = grid.calculatePoints(0, 0, Suit.White)
      points should be(2) // 2
    }

    "return 0 if suit does not match rectangle color" in {
      val grid = createGridWithCard(0, 0, Card(Suit.Red, Value1.Two), Suit.Green)
      val points = grid.calculatePoints(0, 0, Suit.Blue)
      points should be(0) // 2 * 0
    }

    "handle edge case of placing on the last cell" in {
      val grid = new Grid()
      val card = Card(Suit.Green, Value1.Three)
      val result = grid.placeCard(2, 2, card, Suit.White)
      result should be(true)
      grid.calculatePoints(2, 2, Suit.Green) should be(6) // 3 * 2
    }

    "not allow placement outside bounds" in {
      val grid = new Grid()
      val card = Card(Suit.Red, Value1.One)
      grid.placeCard(0, 0, card, Suit.White) should be(true)
      grid.placeCard(0, 0, card, Suit.White) should be(false) // already occupied
      grid.placeCard(3, 3, card, Suit.White) should be(false) // out of bounds
    }

    "initialize with empty positions" in {
      val grid = new Grid()
      grid.calculatePoints(0, 0, Suit.Red) should be(0) // Ensure all positions are empty
    }

    "show all empty positions initially" in {
      val grid = new Grid()
      val output = new java.io.ByteArrayOutputStream()
      Console.withOut(output) {
        grid.display()
      }
      output.toString should include("Empty | Empty | Empty") // Check output for all rows
    }

    "return true when placing on the top-right corner" in {
      val grid = new Grid()
      val card = Card(Suit.Red, Value1.One)
      val result = grid.placeCard(0, 2, card, Suit.White) // Top-right corner
      result should be(true)
      grid.calculatePoints(0, 2, Suit.Red) should be(2) // 1 * 2
    }

    "return true when placing on the bottom-left corner" in {
      val grid = new Grid()
      val card = Card(Suit.Green, Value1.Two)
      val result = grid.placeCard(2, 0, card, Suit.White) // Bottom-left corner
      result should be(true)
      grid.calculatePoints(2, 0, Suit.Green) should be(4) // 2 * 2
    }

    "show the correct card representation when a card is placed" in {
      val grid = new Grid()
      val card = Card(Suit.Red, Value1.One)

      // Place the card at position (1, 1)
      grid.placeCard(1, 1, card, Suit.White)

      // Capture the output of the display method
      val output = new java.io.ByteArrayOutputStream()
      Console.withOut(output) {
        grid.display()
      }

      // Check that the output contains the correct representation of the card
      output.toString should include("One of Red") // Adjust according to your `Value1` and `Suit` implementation
    }
  }
}
