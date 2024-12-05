package util

import model.{Grid, Player}
import model.cards.NumberCards

class PlaceCardCommand(grid: Grid, card: NumberCards, player: Player, points: Int, position: (Int, Int), currentPlayerIndex: Int) extends Command {
  private var previousState: Option[GameState] = None

  override def execute(): Unit = {
    previousState = Some(new GameState(grid.copy(), List(player), currentPlayerIndex)) // Save the current state
    grid.placeCard(position._1, position._2, card) // Place the card on the grid
    player.addPoints(points) // Update the player's points
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