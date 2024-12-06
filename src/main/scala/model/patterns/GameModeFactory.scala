// src/main/scala/model/GameModeFactory.scala
package model.patterns

import model.*
import model.Logik.Game

object GameModeFactory {

  def getGameMode(mode: String, game: Game, strategy: Option[Strategy] = None): GameMode = {
    createGameMode(mode, game, strategy)
  }

  def createGameMode(mode: String, game: Game, strategy: Option[Strategy] = None): GameMode = {
    mode.toLowerCase match {
      case "singleplayer" | "s" => new SinglePlayerMode(game, PlayerRepositoryImpl)
      case "multiplayer" | "m" =>
        val selectedStrategy = strategy.getOrElse(new RandomStrategy())
        new MultiPlayerMode(game, selectedStrategy)
      case _ => throw new IllegalArgumentException("Invalid game mode")
    }
  }
}