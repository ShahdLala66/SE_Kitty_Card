package model.patterns

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameModeSpec extends AnyWordSpec with Matchers {

    "GameMode" should {

        "play the game from start to end" in {
            var startCalled = false
            var playTurnCalled = false
            var endCalled = false
            var turnCount = 0

            val gameMode = new GameMode {
                override def startGame(): Unit = startCalled = true
                override def playTurn(): Unit = {
                    playTurnCalled = true
                    turnCount += 1
                }
                override def endGame(): Unit = endCalled = true
                override def isGameOver: Boolean = turnCount >= 3
            }

            gameMode.playGame()
            
            turnCount should be(3)
        }
    }
}