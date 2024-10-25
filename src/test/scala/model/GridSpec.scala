import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model.{Grid, Card, Suit, Value1}

class GridSpec extends AnyWordSpec with Matchers {

  "A Grid" should {

    "initialize with an empty grid" in {
      val grid = Grid()
      for (x <- 0 until grid.size; y <- 0 until grid.size) {
        grid.placeCard(x, y, Card(Suit.Blue, Value1.One), Suit.Blue) shouldEqual true
      }
    }

    "not allow placing a card out of bounds" in {
      val grid = Grid()
      grid.placeCard(-1, 0, Card(Suit.Blue, Value1.One), Suit.Blue) shouldEqual false
      grid.placeCard(0, -1, Card(Suit.Blue, Value1.One), Suit.Blue) shouldEqual false
      grid.placeCard(grid.size, 0, Card(Suit.Blue, Value1.One), Suit.Blue) shouldEqual false
      grid.placeCard(0, grid.size, Card(Suit.Blue, Value1.One), Suit.Blue) shouldEqual false
    }

    "not allow placing a card in an already occupied spot" in {
      val grid = Grid()
      grid.placeCard(0, 0, Card(Suit.Blue, Value1.One), Suit.Blue) shouldEqual true
      grid.placeCard(0, 0, Card(Suit.Red, Value1.Two), Suit.Red) shouldEqual false
    }

    "calculate points correctly" in {
      val grid = Grid()
      grid.placeCard(0, 0, Card(Suit.Blue, Value1.One), Suit.Blue)
      grid.calculatePoints(0, 0, Suit.Blue) shouldEqual 2
      grid.calculatePoints(0, 0, Suit.White) shouldEqual 1
      grid.calculatePoints(0, 0, Suit.Red) shouldEqual 0
    }

    "display the grid correctly" in {
      val grid = Grid()
      grid.placeCard(0, 0, Card(Suit.Blue, Value1.One), Suit.Blue)
      grid.display()
    }

    "return a correct string representation of the grid" in {
      val grid = Grid()
      grid.placeCard(0, 0, Card(Suit.Blue, Value1.One), Suit.Blue)
      val expectedMesh =
        """One of Blue | Empty | Empty
          |Empty | Empty | Empty
          |Empty | Empty | Empty""".stripMargin
      grid.mesh() shouldEqual expectedMesh
    }
  }
}