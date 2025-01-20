package model.FileIO

import controller.GameController
import model.baseImp.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import util.command.GameState
import util.grid.GridUtils

class FileIOJSONSpec extends AnyWordSpec {

    "A FileIOJSON" should {

        "save the game state to a JSON file" in {
            val fileIO = new FileIOJSON
            val grid = GridUtils.createEmptyGrid(3)
            val players = List(Player("Alice"), Player("Bob"))
            val gameState = new GameState(grid, players, 0, 0)
            val deck = new Deck()
            val hand = new Hand()
            val gameController = new GameController(deck, hand, fileIO) {
                override def getCurrentState: GameState = gameState
                override def getPlayers: List[Player] = players
            }

            val result = fileIO.save(gameController, grid)
            result shouldBe "Game saved successfully"
        }

        "correctly create a NumberCards instance from a valid JSON node" in {
            val fileIO = new FileIOJSON
            val cardJson = Json.obj(
                "suit" -> "Red",
                "value" -> "Five",
                "color" -> "Red"
            )

            val card = fileIO.createCard(cardJson)
            card.suit shouldBe Suit.Red
            card.value shouldBe Value.Five
            card.getColor should not be empty
        }
    }
}