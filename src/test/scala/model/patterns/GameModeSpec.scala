// src/test/scala/model/patterns/GameModeSpec.scala
package model.patterns

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameModeSpec extends AnyWordSpec with Matchers {

    "GameMode" should {
        "execute the game flow correctly" in {
            val gameMode: GameMode = new GameMode {
                private var _turns = 0
                def turns: Int = _turns
                def turns_=(value: Int): Unit = { _turns = value }
                def startGame(): Unit = {}
                def playTurn(): Unit = { turns += 1 }
                def endGame(): Unit = {}
                def isGameOver: Boolean = turns >= 3
            }

            gameMode.playGame()
        }

        "end immediately if the game is already over" in {
            val gameMode = new GameMode {
                def startGame(): Unit = {}
                def playTurn(): Unit = {}
                def endGame(): Unit = {}
                def isGameOver: Boolean = true
            }

            gameMode.playGame()
        }

        "handle a game with no turns" in {
            val gameMode: GameMode = new GameMode {
                private var _turns = 0
                def turns: Int = _turns
                def turns_=(value: Int): Unit = { _turns = value }
                def startGame(): Unit = {}
                def playTurn(): Unit = { turns += 1 }
                def endGame(): Unit = {}
                def isGameOver: Boolean = turns >= 0
            }

            gameMode.playGame()
        }
    }
}