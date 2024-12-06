// src/main/scala/model/RandomStrategy.scala
package model.patterns

import model.Logik.Game

class RandomStrategy extends Strategy {
  override def playTurn(game: Game): Unit = {
      game.handlePlayerTurn()
      game.switchTurns()
    }
}