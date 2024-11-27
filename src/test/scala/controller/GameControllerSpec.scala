// src/test/scala/controller/GameControllerSpec.scala
package controller

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import util.{GameEvent, Observer}

class GameControllerSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "GameController" should {

        "prompt for player name and return the input" in {
            val controller = new GameController()
            val input = "Alice"
            val inputStream = new java.io.ByteArrayInputStream(input.getBytes)
            Console.withIn(inputStream) {
                val result = controller.promptForPlayerName("Player 1")
                result should be(input)
            }
        }

        "handle empty input for player name" in {
            val controller = new GameController()
            val input = ""
            val inputStream = new java.io.ByteArrayInputStream(input.getBytes)
            Console.withIn(inputStream) {
                val result = controller.promptForPlayerName("Player 1")
                result should be("Anonym")
            }
        }

        "handle long input for player name" in {
            val controller = new GameController()
            val input = "A" * 1000
            val inputStream = new java.io.ByteArrayInputStream(input.getBytes)
            Console.withIn(inputStream) {
                val result = controller.promptForPlayerName("Player 1")
                result should be(input)
            }
        }

        "set the observer correctly when setObserver is called" in {
            val controller = new GameController()
            val observer = mock[Observer]
            controller.setObserver(observer)
            controller.update(mock[GameEvent]) // Trigger an update to verify observer is set
            verify(observer, times(1)).update(any[GameEvent])
        }


        "print the correct message for displayBadChoice" in {
            val controller = new GameController()
            val color = "red"
            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                controller.displayBadChoice(color)
            }
            output.toString should include(s"Bad choice: $color")
        }

        "print the correct message for displayCatInColor" in {
            val controller = new GameController()
            val color = "blue"
            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                controller.displayCatInColor(color)
            }
            output.toString should include(s"Cat in color: $color")
        }



        "print the correct message for displayMeh" in {
            val controller = new GameController()
            val color = "green"
            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                controller.displayMeh(color)
            }
            output.toString should include(s"Meh: $color")
        }
    }
}
