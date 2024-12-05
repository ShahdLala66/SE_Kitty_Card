// src/main/scala/util/PlaceCardCommand.scala
package util

import model.{Grid, Player}
import model.cards.NumberCards

class PlaceCardCommand(grid: Grid, card: NumberCards, player: Player, points: Int, position: (Int, Int)) extends Command {
  private val previousCard = grid.getCard(position._1, position._2)
  private val previousColor = grid.getColor(position._1, position._2)

  override def execute(): Unit = {
    previousState = new GameState(grid, List(player.copy()), 0, player.points) // Save the previous state
    grid.placeCard(position._1, position._2, card)
    player.addPoints(points)
  }

  override def undo(): Unit = {
    grid.removeCard(position._1, position._2)
    grid.setColor(position._1, position._2, previousColor)
    player.addPoints(-points)
    // Restore the previous state
    grid.updateGrid(previousState.getGrid)
    previousState.getPlayers.foreach(p => p.updateHand(previousState.getPlayers.head.getHand))
    previousState.getPlayers.foreach(p => p.updatePoints(previousState.getPoints))
  }

  override def redo(): Unit = {
    execute() // Reapply the execute method
  }
}