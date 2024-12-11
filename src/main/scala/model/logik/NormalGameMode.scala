package model.logik

import logik.GameModeL
import model.objects.{Deck, Grid, Player}

class NormalGameMode(protected val grid: Grid, protected val deck: Deck) extends GameModeL(grid, deck) {

  override def processPlayerTurn(currentPlayer: Player, opponentPlayer: Player): Unit = {
    currentPlayer.drawCard(deck) // Player draws a card
    println(s"${currentPlayer.name}'s turn. Your hand: ${currentPlayer.getHand}")
    val input = receivePlayerInput() // Input format: "cardIndex x y"
    if (!handleCardPlacement(currentPlayer, input)) {
      println("Invalid move! Try again.")
    }
  }

  /**
   * Simulates receiving player input (placeholder implementation).
   */
  private def receivePlayerInput(): String = {
    // Implementation for receiving input from the player (e.g., CLI or UI integration)
    "0 1 1" // Example: cardIndex 0, place at x=1, y=1
  }
}