package model.patterns

import model.logik.Game
import model.objects.Grid
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
  }
}