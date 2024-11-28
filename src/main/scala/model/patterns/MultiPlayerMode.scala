// src/main/scala/model/patterns/MultiPlayerMode.scala
package model.patterns

import model.{Game, Grid}

class MultiPlayerMode(game: Game, strategy: Strategy) extends GameMode {
  override def startGame(): Unit = {
    game.initialize()
  }

  override def playTurn(): Unit = {
    strategy.playTurn(game)
  }

  override def endGame(): Unit = {
    println("Ending multiplayer game...")
    // Add logic to end multiplayer game
  }

  override def isGameOver(): Boolean = {
    game.getGrid.isFull
  }
}