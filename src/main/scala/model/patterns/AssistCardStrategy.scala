package model.patterns

import model.logik.Game

class AssistCardStrategy extends Strategy {
  override def playTurn(game: Game): Unit = {
    game.handlePlayerTurn()
    game.switchTurns()
  }

}
