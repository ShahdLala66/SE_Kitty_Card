package model.patterns

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.*
import model.logik.Game
import model.objects.{Deck, Grid}
import model.patterns.*

class GameModeFactorySpec extends AnyWordSpec with Matchers {

    "GameModeFactory" should {

        "create a SinglePlayerMode when mode is 'singleplayer'" in {
            val game = new Game(new Deck(), new Grid(3), null)
            val gameMode = GameModeFactory.getGameMode("singleplayer", game)
            gameMode shouldBe a [SinglePlayerMode]
        }

        "create a MultiPlayerMode when mode is 'multiplayer' and strategy is provided" in {
            val game = new Game(new Deck(), new Grid(3), null)
            val strategy = new RandomStrategy()
            val gameMode = GameModeFactory.getGameMode("multiplayer", game, Some(strategy))
            gameMode shouldBe a [MultiPlayerMode]
        }

        "create a MultiPlayerMode with default strategy when mode is 'multiplayer' and no strategy is provided" in {
            val game = new Game(new Deck(), new Grid(3), null)
            val gameMode = GameModeFactory.getGameMode("multiplayer", game)
            gameMode shouldBe a [MultiPlayerMode]
        }

        "throw an IllegalArgumentException when mode is invalid" in {
            val game = new Game(new Deck(), new Grid(3), null)
            an [IllegalArgumentException] should be thrownBy {
                GameModeFactory.getGameMode("invalidmode", game)
            }
        }
    }
}