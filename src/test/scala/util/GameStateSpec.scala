// src/test/scala/util/GameStateSpec.scala
package util

import model.Objects.{Grid, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.*
import util.command.GameState

class GameStateSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A GameState" should {

        "return the correct grid" in {
            val grid = mock[Grid]
            val players = List(mock[Player])
            val gameState = new GameState(grid, players, 0, 0)

            gameState.getGrid should be (grid)
        }

        "return the correct players" in {
            val grid = mock[Grid]
            val players = List(mock[Player])
            val gameState = new GameState(grid, players, 0, 0)

            gameState.getPlayers should be (players)
        }

        "return the correct current player" in {
            val grid = mock[Grid]
            val player1 = mock[Player]
            val player2 = mock[Player]
            val players = List(player1, player2)
            val gameState = new GameState(grid, players, 1, 0)

            gameState.getCurrentPlayer should be (player2)
        }

        "return the correct points" in {
            val grid = mock[Grid]
            val players = List(mock[Player])
            val gameState = new GameState(grid, players, 0, 10)

            gameState.getPoints should be (10)
        }

        "update the grid correctly" in {
            val grid = mock[Grid]
            val newGrid = mock[Grid]
            val players = List(mock[Player])
            val gameState = new GameState(grid, players, 0, 0)

            val updatedGameState = gameState.updateGrid(newGrid)
            updatedGameState.getGrid should be (newGrid)
        }

        "switch to the next player correctly" in {
            val grid = mock[Grid]
            val player1 = mock[Player]
            val player2 = mock[Player]
            val players = List(player1, player2)
            val gameState = new GameState(grid, players, 0, 0)

            val nextGameState = gameState.nextPlayer()
            nextGameState.getCurrentPlayer should be (player2)
        }
        
        "update the grid and points correctly" in {
            val grid = mock[Grid]
            val newGrid = mock[Grid]
            val players = List(mock[Player])
            val gameState = new GameState(grid, players, 0, 0)

            val updatedGameState = gameState.update(newGrid, 20)
            updatedGameState.getGrid should be (newGrid)
            updatedGameState.getPoints should be (20)
        }
    }
}