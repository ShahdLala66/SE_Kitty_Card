package logik

import model.objects.cards.NumberCards
import model.objects.{Deck, Grid, Player}
import util.command.CommandManager

import scala.util.Try

abstract class GameModeL(protected val grid: Grid, protected val deck: Deck) {
  private var isTurnSkipped: Boolean = false

  /**
   * Main game loop that iterates until the grid is full or the game ends.
   */
  def startGameLoop(currentPlayer: Player, opponentPlayer: Player): Unit = {
    while (!isGridFull) { // Continue until the grid is full
      processPlayerTurn(currentPlayer, opponentPlayer)

      // Check if the turn is skipped
      if (!isTurnSkipped) {
        // Switch between players after each turn
        val temp = currentPlayer
        currentPlayer = opponentPlayer
        opponentPlayer = temp
      } else {
        // Reset the turn skipped state for the next turn
        isTurnSkipped = false
        currentPlayer = temp
      }
    }
  }

  /**
   * Handles the specific turn logic for the game mode (implemented by subclasses).
   */
  def processPlayerTurn(currentPlayer: Player, opponentPlayer: Player): Unit

  /**
   * Checks whether the grid is full, ending the game.
   */
  protected def isGridFull: Boolean = grid.isFull

  /**
   * Shared card placement logic for `NumberCards`.
   */
  protected def handleCardPlacement(player: Player, input: String): Boolean = {
    Try {
      val parts = input.split(" ")
      val cardIndex = parts(0).toInt
      val x = parts(1).toInt
      val y = parts(2).toInt
      player.getHand.lift(cardIndex) match {
        case Some(card: NumberCards) =>
          if (grid.placeCard(card, x, y)) { // Attempt to place card directly on grid
            val points = grid.calculatePoints(x, y)
            player.addPoints(points)
            player.removeCard(card) // Remove the card from the player's hand
            true
          } else false // Placement failed
        case _ => false // Invalid card type or index
      }
    }.getOrElse(false)
  }

  /**
   * Sets the turn skipped state.
   */
  protected def setTurnSkipped(skipped: Boolean): Unit = {
    isTurnSkipped = skipped
  }
}