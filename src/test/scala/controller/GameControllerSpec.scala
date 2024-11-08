package controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameControllerSpec extends AnyWordSpec with Matchers {

    "GameController" should {
        "initialize deck, grid, and catPrint correctly" in {
            val gameController = new GameController

            gameController.getDeck should not be null
            gameController.getGrid should not be null
            gameController.getCatPrint should not be null
        }
    }
}