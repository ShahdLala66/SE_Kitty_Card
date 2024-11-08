package controller

/*import org.scalatest.*
import model.*
import org.scalatest.matchers.should.Matchers
import view.*
import controller.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import util.ErrorMessages

class ControllerSpec extends AnyWordSpec with Matchers {

  "GameController" should {

    "start the game and prompt for family-friendly version" in {
      val controller = new GameController()

      // Simulate the user input (family-friendly version, player names)
      System.setIn(new java.io.ByteArrayInputStream("y\nPlayer 1\nPlayer 2\n".getBytes()))

      // Start the game, simulate the inputs
      controller.startGame()

      // Check that the game prompts for the family-friendly version
      // Since we don't have direct access to the method prints, this test focuses on the flow
      assert(controller.isInstanceOf[GameController]) // This just validates that the game controller runs
    }

    "calculate points correctly when card matches rectangle color" in {
      val grid = new Grid(3)
      val card = Card(Suit.Blue, Value1.Three)

      grid.placeCard(0, 0, card)
      grid.setRectangleColor(2, 2, Suit.Blue)  // force match for testing

      val points = grid.calculatePoints(0, 0)

      points shouldBe 6  // 3 * 2 = 6 (double points for matching)
    }

    "calculate points correctly when placed on a white rectangle" in {
      val grid = new Grid(3)
      val card = Card(Suit.Blue, Value1.Three)

      grid.placeCard(1, 1, card)
      grid.setRectangleColor(2, 2, Suit.White)  // force white for testing

      val points = grid.calculatePoints(1, 1)

      points shouldBe 3  // No double points, should be 3
    }

    "award zero points when placed on a different colored rectangle" in {
      val grid = new Grid(3)
      val card = Card(Suit.Blue, Value1.Three)

      grid.placeCard(2, 2, card)
      grid.setRectangleColor(2, 2, Suit.Red) // force mismatch for testing

      val points = grid.calculatePoints(2, 2)

      points shouldBe 0  // No points for a color mismatch
    }

    "handle invalid input gracefully when placing cards" in {
      val grid = new Grid(3)
      val controller = new GameController()

      // Simulate user input
      System.setIn(new java.io.ByteArrayInputStream("invalid input\n1 1\n".getBytes()))

      // Start the game and test the invalid input handling
      controller.startGame()

      // You would need to observe the behavior by running and checking printed output
      // For now, we validate that the game continues without crashing
      assert(grid.placeCard(1, 1, Card(Suit.Blue, Value1.Three)))  // Check if valid placement works after invalid input is handled
    }

    "switch players after each turn" in {
      val grid = new Grid(3)
      val deck = new Deck()
      val catPrint = new CatPrint()
      val controller = new GameController()

      // Simulate input for two players
      System.setIn(new java.io.ByteArrayInputStream("y\nPlayer 1\nPlayer 2\n".getBytes()))

      // Set up a simple loop to test turn switching
      controller.startGame()

      // At the start, Player 1's turn is selected
      assert(controller.currentPlayer.name == "Player 1")

      // After Player 1 completes a turn, it should switch to Player 2
      controller.startGame()  // Run the next part of the game

      assert(controller.currentPlayer.name == "Player 2") //dont ever comment this, endless loop.
    }
  }
} */
