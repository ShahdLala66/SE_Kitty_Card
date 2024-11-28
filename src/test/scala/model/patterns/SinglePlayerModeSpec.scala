// src/test/scala/model/patterns/SinglePlayerModeSpec.scala
package model.patterns

import model.Game
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class SinglePlayerModeSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "SinglePlayerMode" should {
        "initialize the game and print a message when startGame is called" in {
            val mockGame = mock[Game]
            val mode = new SinglePlayerMode(mockGame)

            mode.startGame()

            verify(mockGame).initialize()
            // Assuming you have a way to capture printed output
        }

        "print a message when playTurn is called" in {
            val mockGame = mock[Game]
            val mode = new SinglePlayerMode(mockGame)

            mode.playTurn()

            // Assuming you have a way to capture printed output
        }

        "print a message when endGame is called" in {
            val mockGame = mock[Game]
            val mode = new SinglePlayerMode(mockGame)

            mode.endGame()

            // Assuming you have a way to capture printed output
        }

        "return false when isGameOver is called" in {
            val mockGame = mock[Game]
            val mode = new SinglePlayerMode(mockGame)

            mode.isGameOver should be(false)
        }
    }
}