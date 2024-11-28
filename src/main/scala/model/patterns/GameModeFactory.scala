// src/main/scala/model/GameModeFactory.scala
package model.patterns

import model.patterns.GameMode
import model.*

object GameModeFactory {
  def createGameMode(mode: String, game: Game, strategy: Option[Strategy] = None): GameMode = {
    mode.toLowerCase match {
      case "singleplayer" => new SinglePlayerMode(game)
      case "multiplayer" =>
        val selectedStrategy = strategy.getOrElse(new RandomStrategy())
        new MultiPlayerMode(game, selectedStrategy)
      case _ => throw new IllegalArgumentException("Invalid game mode")
    }
  }
}