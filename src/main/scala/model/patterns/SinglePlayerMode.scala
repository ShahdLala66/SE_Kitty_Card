// src/main/scala/model/patterns/SinglePlayerMode.scala
package model.patterns

import model.Game

class SinglePlayerMode(game: Game) extends GameMode {
  override def startGame(): Unit = {
    game.initialize()
    println("Single-player mode is coming soon!")
  }

  override def playTurn(): Unit = {
    println("Single-player mode is coming soon!")
  }

  override def endGame(): Unit = {
    println("Ending single player game...")
    // Add logic to end single player game
  }

  override def isGameOver(): Boolean = {
    // Define the condition to end the single-player game
    false
  }
}