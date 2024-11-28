// src/main/scala/model/patterns/GameMode.scala
package model.patterns

trait GameMode {
  def playGame(): Unit = {
    startGame()
    while (!isGameOver) {
      playTurn()
    }
    endGame()
  }

  def startGame(): Unit
  def playTurn(): Unit
  def endGame(): Unit
  def isGameOver: Boolean
}