// src/test/scala/model/patterns/SinglePlayerModeSpec.scala
package model.patterns

import model.Game
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

import java.io.ByteArrayInputStream

class SinglePlayerModeSpec extends AnyWordSpec with Matchers with MockitoSugar {

  def simulateInput[T](input: String)(block: => T): T = {
    Console.withIn(new ByteArrayInputStream(input.getBytes())) {
      block
    }
  }

  "SinglePlayerMode" should {
    "initialize the game and print a message when startGame is called" in {
      val mockGame = mock[Game]
      val mode = new SinglePlayerMode(mockGame)
      simulateInput("2\nNewPlayer\n") {
        mode.startGame()
      }
    }

    "print a message when playTurn is called" in {
      val mockGame = mock[Game]
      val mode = new SinglePlayerMode(mockGame)

      simulateInput("") {


      }
      simulateInput("2\nNewPlayer\n"){
        
        mode.playTurn()
        }
      // Assuming you have a way to capture printed output
    }

    "print a message when endGame is called" in {
      val mockGame = mock[Game]
      val mode = new SinglePlayerMode(mockGame)

      
      mode.endGame()
    }

    "return false when isGameOver is called" in {
      val mockGame = mock[Game]
      val mode = new SinglePlayerMode(mockGame)

      simulateInput("false\n") {
        mode.isGameOver() should be(false)
      }
    }
  }
}