import model.Suit.Suit
import org.scalatest.funsuite.AnyFunSuite
import model.{Card, Grid, Suit, Value1}

class GridSpec extends AnyFunSuite {

  private def createGridWithCard(x: Int, y: Int, card: Card, rectangleColor: Suit): Grid = {
    val grid = new Grid()
    grid.placeCard(x, y, card, rectangleColor)
    grid
  }

  test("Placing a card in an empty spot should succeed") {
    val grid = new Grid()
    val card = Card(Suit.Blue, Value1.Four)
    assert(grid.placeCard(0, 0, card, Suit.Blue))
  }

  test("Placing a card on a matching colored rectangle should double its value") {
    val grid = createGridWithCard(0, 0, Card(Suit.Red, Value1.Three), Suit.Red)
    assert(grid.calculatePoints(0, 0, Suit.Red) == 6)
  }

  test("Placing a card on a different colored rectangle should give zero points") {
    val grid = createGridWithCard(0, 0, Card(Suit.Green, Value1.Two), Suit.Purple)
    assert(grid.calculatePoints(0, 0, Suit.Purple) == 0)
  }

  test("Placing a card on a blank (white) rectangle should retain its value") {
    val grid = createGridWithCard(0, 0, Card(Suit.Purple, Value1.Five), Suit.White)
    assert(grid.calculatePoints(0, 0, Suit.White) == 5)
  }

  test("Cannot place a card in an already occupied spot") {
    val grid = new Grid()
    val card1 = Card(Suit.Blue, Value1.Four)
    val card2 = Card(Suit.Red, Value1.Six)
    grid.placeCard(0, 0, card1, Suit.Blue)
    assert(!grid.placeCard(0, 0, card2, Suit.Red))
  }
}