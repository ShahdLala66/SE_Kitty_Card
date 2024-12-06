package model

import model.logik.Game
import model.objects.cards.NumberCards
import model.objects.{Deck, Grid}
import model.patterns.GameMode
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class GameSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "Game" should {

        "initialize the game correctly" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val gameMode = mock[GameMode]
            val game = new Game(deck, grid, gameMode)

            when(deck.drawCard()).thenReturn(Some(mock[NumberCards]))

            game.start("Player1", "Player2")

            game.player1.name should be("Player1")
            game.player2.name should be("Player2")
            game.currentPlayer should be(game.player1)
            verify(grid).displayInitialColors()
            verify(gameMode).startGame()
        }

        "distribute initial cards to players" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val gameMode = mock[GameMode]
            val game = new Game(deck, grid, gameMode)

            when(deck.drawCard()).thenReturn(Some(mock[NumberCards]))

            game.start("Player1", "Player2")

            verify(deck, times(6)).drawCard()
        }

        "handle player turn correctly" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val gameMode = mock[GameMode]
            val game = new Game(deck, grid, gameMode)

            when(deck.drawCard()).thenReturn(Some(mock[NumberCards]))

            // Mocking user input
            val inputStream = new java.io.ByteArrayInputStream("draw\n".getBytes)
            Console.withIn(inputStream) {
                game.start("Player1", "Player2")
                game.handlePlayerTurn()
            }

            verify(deck, times(8)).drawCard()
        }
    }
}