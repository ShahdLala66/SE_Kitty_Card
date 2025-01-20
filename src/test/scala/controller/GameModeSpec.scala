package controller

import model.baseImp.{Grid, Player}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import util.command.GameState
import util.{InitializeGUIForLoad, UpdateLoadedGame}

class GameModeSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "SinglePlayerMode" should {
        
        "load a game" in {
            val controller = mock[GameController]
            val savedState = mock[GameState]
            val mode = new SinglePlayerMode

            mode.loadGame(controller, savedState)

            // Verify the expected behavior
            verify(controller, never()).grid = any[Grid]
            verify(controller, never()).startMultiPlayerGame()
        }
    }

    "MultiPlayerMode" should {
        "start the game" in {
            val controller = mock[GameController]
            val mode = new MultiPlayerMode

            mode.startGame(controller)

            verify(controller).grid = any[Grid]
            verify(controller).startMultiPlayerGame()
        }

        "load a game" in {
            val controller = mock[GameController]
            val savedState = mock[GameState]
            val grid = mock[Grid]
            val player1 = mock[Player]
            val player2 = mock[Player]
            val currentPlayer = mock[Player]

            when(savedState.getGrid).thenReturn(grid)
            when(savedState.getPlayers).thenReturn(List(player1, player2))
            when(savedState.getCurrentPlayer).thenReturn(currentPlayer)
            when(controller.currentPlayer).thenReturn(currentPlayer) // Ensure currentPlayer is mocked

            val mode = new MultiPlayerMode

            mode.loadGame(controller, savedState)

            verify(controller).notifyObservers(InitializeGUIForLoad)
            verify(controller).grid = grid
            verify(controller).player1 = player1
            verify(controller).player2 = player2
            verify(controller).currentPlayer = currentPlayer
            verify(controller, times(2)).notifyObservers(any[UpdateLoadedGame]) // Expect notifyObservers to be called twice
            verify(controller).startGameLoop()
        }
    }
}