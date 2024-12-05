package util

import model.cards.{Card, NumberCards}
import model.{Grid, Player}

class RemoveCardCommand(grid: Grid, player: Player, points: Int, removedCard: Option[NumberCards], position: (Int, Int)) extends Command {
  private var previousState: Option[GameState] = None

  override def execute(): Unit = {
    previousState = Some(new GameState(grid.copy(), List(player), 0)) // Save the current state
    removedCard match {
      case Some(card) =>
        if (grid.placeCard(position._1, position._2, card)) {
          player.addPoints(-points) // Deduct points from the player
        }
      case None => // No card to remove
    }
  }

  override def undo(): Unit = {
    previousState.foreach { state =>
      state.getGrid.display() // Use the observer to update the grid
      player.setPoints(state.getCurrentPlayer.points) // Restore the player's points
    }
  }

  override def redo(): Unit = {
    execute() // Reapply the execute method
  }
}