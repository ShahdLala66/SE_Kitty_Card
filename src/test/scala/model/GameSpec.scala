package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model._
import model.cards.{NumberCards, Suit, Value}
import controller.GameController

class GameSpec extends AnyWordSpec with Matchers {

    "A Game" should {

        "display a welcome message" in {
            val deck = new Deck()
            val grid = Grid()
            val player1 = Player("Player 1")
            val player2 = Player("Player 2")
            val game = new Game(player1, player2, deck, grid)

            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                game.welcomeMessage()
            }

            val result = output.toString
            result should include("Welcome to the Kitty Card Game!")
            result should include("Players take turns drawing and placing cards on the grid.")
            result should include("Earn points by placing cards on matching colors or white squares.")
        }

        "distribute initial cards to players" in {
            val deck = new Deck()
            val grid = Grid()
            val player1 = Player("Player 1")
            val player2 = Player("Player 2")
            val game = new Game(player1, player2, deck, grid)

            game.distributeInitialCards()

            player1.getHand.getCards.size
            player2.getHand.getCards.size shouldBe 3
        }

        "switch turns between players" in {
            val deck = new Deck()
            val grid = Grid()
            val player1 = Player("Player 1")
            val player2 = Player("Player 2")
            val game = new Game(player1, player2, deck, grid)

            game.switchTurns()
            game.currentPlayer shouldBe player2

            game.switchTurns()
            game.currentPlayer shouldBe player1
        }

        "display final scores correctly" in {
            val deck = new Deck()
            val grid = Grid()
            val player1 = Player("Player 1")
            val player2 = Player("Player 2")
            val game = new Game(player1, player2, deck, grid)

            player1.addPoints(10)
            player2.addPoints(15)

            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                game.displayFinalScores()
            }

            val result = output.toString
            result should include ("Player 1's final score: 10")
            result should include ("Player 2's final score: 15")
            result should include ("Player 2 wins!")
        }



        "process player input correctly" in {
            val deck = new Deck()
            val grid = Grid()
            val player1 = Player("Player 1")
            val player2 = Player("Player 2")
            val game = new Game(player1, player2, deck, grid)
            val controller = new GameController()

            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                Console.withIn(new java.io.ByteArrayInputStream("draw\n".getBytes)) {
                    game.processPlayerInput(controller)
                }
            }

            val result = output.toString
            result should include ("drew another card and ended their turn.")
        }

        "handle card placement correctly" in {
            val deck = new Deck()
            val grid = Grid()
            val player1 = Player("Player 1")
            val player2 = Player("Player 2")
            val game = new Game(player1, player2, deck, grid)
            val controller = new GameController()

            val card = NumberCards(Suit.Green, Value.Two)
            player1.getHand.addCard(card)

            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                game.handleCardPlacement("0 0 0", controller) shouldBe true
            }

            val result = output.toString
            result should include ("earned")
        }
    }
}