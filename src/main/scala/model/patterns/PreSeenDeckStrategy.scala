// src/main/scala/model/PreSeenDeckStrategy.scala
package model.patterns

import model.Game

class PreSeenDeckStrategy extends Strategy {
  override def playTurn(game: Game): Unit = {
    // Placeholder logic for the pre-seen deck strategy
    println("Pre-seen deck strategy is not ydevolped yet lolllllll")
    game.handlePlayerTurn()
    game.switchTurns()
  }
}