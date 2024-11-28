package model.patterns

trait GameMode {
  def playGame(): Unit = {
    startGame()
    while (!isGameOver()) { //this is the template method
      playTurn()
    }
    endGame()
  }

  def startGame(): Unit
  def playTurn(): Unit
  def endGame(): Unit
  def isGameOver(): Boolean
}