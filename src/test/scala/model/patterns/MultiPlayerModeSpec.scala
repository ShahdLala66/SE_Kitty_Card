package model.patterns

import model.{Game, Grid}
import org.mockito.Mockito.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class MultiPlayerModeSpec extends AnyWordSpec with MockitoSugar {

  "MultiPlayerModeSpec" should {

    "startGame" in {
      val mockGame = mock[Game]
      val mockStrategy = mock[Strategy]
      val multiPlayerMode = new MultiPlayerMode(mockGame, mockStrategy)

      multiPlayerMode.startGame()

      verify(mockGame).initialize()
    }

    "playTurn" in {
      val mockGame = mock[Game]
      val mockStrategy = mock[Strategy]
      val multiPlayerMode = new MultiPlayerMode(mockGame, mockStrategy)

      multiPlayerMode.playTurn()

      verify(mockStrategy).playTurn(mockGame)
    }

    "endGame" in {
      val captureOutput = new java.io.ByteArrayOutputStream()
      Console.withOut(captureOutput) {
        val mockGame = mock[Game]
        val mockStrategy = mock[Strategy]
        val multiPlayerMode = new MultiPlayerMode(mockGame, mockStrategy)

        multiPlayerMode.endGame()

        assert(captureOutput.toString.contains("Ending multiplayer game..."))
      }
    }

    "isGameOver" in {
      val mockGrid = mock[Grid]
      val mockGame = mock[Game]
      when(mockGame.getGrid).thenReturn(mockGrid)
      val mockStrategy = mock[Strategy]
      val multiPlayerMode = new MultiPlayerMode(mockGame, mockStrategy)

      when(mockGrid.isFull).thenReturn(true)
      assert(multiPlayerMode.isGameOver())

      when(mockGrid.isFull).thenReturn(false)
      assert(!multiPlayerMode.isGameOver())
    }

    // Mock `playGame` related components if available, if not, the test might be omitted or defined based on the available logic.

  }
}