package controller

import model.FileIO.FileIOXML
import model.baseImp.{Deck, Grid, Hand, Player}
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import util.{AskForGameMode, AskForLoadGame, GameOver, Observer}

class GameControllerSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A GameController" should {

        "start the game and notify observers" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            controller.startGame()

            verify(observer).update(AskForGameMode)
        }

        "set the game mode and notify observers" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            controller.setGameMode("s")

            controller.gameMode shouldBe a [SinglePlayerMode]
            verify(observer).update(AskForLoadGame)
        }

        "notify observers when game is over" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)
            val player1 = Player("Alice")
            val player2 = Player("Bob")
            controller.player1 = player1
            controller.player2 = player2
            controller.currentPlayer = player1
            val grid = mock[Grid]
            controller.grid = grid

            when(grid.isFull).thenReturn(true)

            controller.handleCardPlacement(0, 0, 0)

            verify(observer).update(GameOver(player1.name, player1.points, player2.name, player2.points))
        }
    }
}