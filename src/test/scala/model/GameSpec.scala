package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.GameCallbacks

class GameSpec extends AnyWordSpec with Matchers {

    "A Game" should {

        "initialize correctly with two players, a deck, and a grid" in {
            val player1 = new Player("Player 1")
            val player2 = new Player("Player 2")
            val deck = new Deck()
            val grid = new Grid()

            val game = new Game(player1, player2, deck, grid)

            game.currentPlayer should be(player1)
        }

        "distribute initial cards to both players" in {
            val player1 = new Player("Player 1")
            val player2 = new Player("Player 2")
            val deck = new Deck()
            val grid = new Grid()

            val game = new Game(player1, player2, deck, grid)
            game.distributeInitialCards()

            player1.getHand should not be null
            player2.getHand should not be null
        }

        "switch turns between players" in {
            val player1 = new Player("Player 1")
            val player2 = new Player("Player 2")
            val deck = new Deck()
            val grid = new Grid()

            val game = new Game(player1, player2, deck, grid)
            game.switchTurns()

            game.currentPlayer should be(player2)
            game.switchTurns()
            game.currentPlayer should be(player1)
        }

        "handle card placement correctly" in {
            val player1 = new Player("Player 1")
            val player2 = new Player("Player 2")
            val deck = new Deck()
            val grid = new Grid()

            val game = new Game(player1, player2, deck, grid)
            game.distributeInitialCards()

            val input = "0 1 2"
            val result = game.handleCardPlacement(input)

            result should be(true)
        }
    }
}