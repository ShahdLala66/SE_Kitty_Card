package model.FileIO

import controller.GameController
import model.baseImp.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import util.command.GameState
import util.grid.GridUtils

import scala.xml.*


class FileIOXMLSpec extends AnyWordSpec {

    "A FileIOXML" should {

        "save the game state to an XML file" in {
            val fileIO = new FileIOXML
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

        "load the game state from an XML file" in {
            val fileIO = new FileIOXML
            val deck = new Deck()
            val hand = new Hand()
            val gameController: GameController = new GameController(deck, hand, fileIO) {
                var loadedState: GameState = _

                override def getCurrentState: GameState = ???

                override def getPlayers: List[Player] = ???

                override def loadGameState(state: GameState): Unit = {
                    loadedState = state
                }
            }

            val result = fileIO.load(gameController)
            result shouldBe "Game loaded successfully"
        }

        "correctly create a NumberCards instance from a valid XML node" in {
            val fileIO = new FileIOXML
            val cardNode: Node =
                <card>
                    <suit>Red</suit>
                    <value>Five</value>
                    <color>Red</color>
                </card>

            val card = fileIO.createCard(cardNode)
            card.suit shouldBe Suit.Red
            card.value shouldBe Value.Five
            card.getColor should not be empty
        }


    }
}
