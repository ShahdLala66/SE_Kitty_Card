// src/test/scala/controller/GameControllerSpec.scala
package controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model.{Deck, Grid, Player}
import util.GameCallbacks

class GameControllerSpec extends AnyWordSpec with Matchers {

    "A GameController" should {

        "initialize correctly" in {
            val controller = new GameController()
            controller should not be null
        }

        "display cat in color" in {
            val controller = new GameController()
            noException should be thrownBy controller.displayCatInColor("Blue")
        }

        "display bad choice" in {
            val controller = new GameController()
            noException should be thrownBy controller.displayBadChoice("Red")
        }

        "display meh" in {
            val controller = new GameController()
            noException should be thrownBy controller.displayMeh("Green")
        }
    }
}
