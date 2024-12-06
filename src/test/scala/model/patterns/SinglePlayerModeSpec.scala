package model.patterns

import model.{Game, Player}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.ByteArrayInputStream

class SinglePlayerModeSpec extends AnyWordSpec with Matchers with MockFactory {

    def simulateInput[T](input: String)(block: => T): T = {
        Console.withIn(new ByteArrayInputStream(input.getBytes())) {
            block
        }
    }

    def toPlayerData(players: List[Player]): List[PlayerData] = {
        players.map(player => PlayerData(player.name, player.points, 1, 0)) // Provide default values for level and money
    }

    "SinglePlayerMode" should {
        "initialize the game and print a message when startGame is called" in {
            val mockGame = mock[Game]
            val mockPlayerRepository = mock[PlayerRepository]
            (mockPlayerRepository.addPlayer _).expects(*).once() // Expect the addPlayer method to be called once
            val mode = new SinglePlayerMode(mockGame, mockPlayerRepository)
            simulateInput("2\nNewPlayer\n") {
                mode.startGame()
            }
        }

        "print a message when playTurn is called" in {
            val mockGame = mock[Game]
            val mockPlayerRepository = mock[PlayerRepository]
            val mode = new SinglePlayerMode(mockGame, mockPlayerRepository)
            mode.playTurn()
        }

        "print a message when endGame is called" in {
            val mockGame = mock[Game]
            val mockPlayerRepository = mock[PlayerRepository]
            val mode = new SinglePlayerMode(mockGame, mockPlayerRepository)
            mode.endGame()
        }

        "return false for isGameOver" in {
            val mockGame = mock[Game]
            val mockPlayerRepository = mock[PlayerRepository]
            val mode = new SinglePlayerMode(mockGame, mockPlayerRepository)
            mode.isGameOver shouldBe false
        }

        "select an existing player when a valid choice is made" in {
            val mockGame = mock[Game]
            val mockPlayerRepository = mock[PlayerRepository]
            val players = List(Player("Player1", 100), Player("Player2", 200))
            (mockPlayerRepository.loadPlayers _).expects().returning(toPlayerData(players)).once() // Expect the loadPlayers method to be called once

            val mode = new SinglePlayerMode(mockGame, mockPlayerRepository)
            simulateInput("1\n") { // Simulate user input for selecting the first player
                val selectedPlayer = mode.selectExistingPlayer()
                selectedPlayer.points should be >= 0
            }
        }

        "create a new player when no existing players are found" in {
            val mockGame = mock[Game]
            val mockPlayerRepository = mock[PlayerRepository]
            (mockPlayerRepository.loadPlayers _).expects().returning(Nil)

            val mode = new SinglePlayerMode(mockGame, mockPlayerRepository) {
                override def createNewPlayer(): Player = {
                    Player("NewPlayer")
                }
            }

            val newPlayer = mode.selectExistingPlayer()
            newPlayer.points shouldBe 0
        }
    }
    "select an existing player when a valid choice is made" in {
        val mockGame = mock[Game]
        val mockPlayerRepository = mock[PlayerRepository]
        val players = List(Player("Player1", 100), Player("Player2", 200))
        (mockPlayerRepository.loadPlayers _).expects().returning(toPlayerData(players)).once() // Expect the loadPlayers method to be called once

        val mode = new SinglePlayerMode(mockGame, mockPlayerRepository)
        simulateInput("1\n") { // Simulate user input for selecting the first player
            val selectedPlayer = mode.selectExistingPlayer()
            selectedPlayer.points shouldBe 100
        }
    }
    "create a new player when an invalid choice is made" in {
        val mockGame = mock[Game]
        val mockPlayerRepository = mock[PlayerRepository]
        (mockPlayerRepository.loadPlayers _).expects().never() // Expect the loadPlayers method to never be called
        (mockPlayerRepository.addPlayer _).expects(PlayerData("", 0, 1, 0)).once() // Expect the addPlayer method to be called once with empty name

        val mode = new SinglePlayerMode(mockGame, mockPlayerRepository)
        simulateInput("invalid\n") { // Simulate invalid user input
            val newPlayer = mode.selectOrCreatePlayer()
            newPlayer.name shouldBe empty
            newPlayer.points shouldBe 0
        }
    }

}