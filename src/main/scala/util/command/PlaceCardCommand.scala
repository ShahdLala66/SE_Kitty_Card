// src/main/scala/util/PlaceCardCommand.scala
package util.command

import model.objects.cards.NumberCards
import model.objects.{Grid, Player}

class
PlaceCardCommand(grid: Grid, card: NumberCards, player: Player, points: Int, position: (Int, Int)) extends CommandTrait {
  private val previousCard = grid.getCard(position._1, position._2)
  private val previousColor = grid.getColor(position._1, position._2)
  private var earnedPoints = 0

  override def execute(): Unit = {
    previousState = new GameState(grid, List(player.copy()), 0, player.points) // Save the previous state
    grid.placeCard(position._1, position._2, card)
    earnedPoints = grid.calculatePoints(position._1, position._2)
    player.addPoints(earnedPoints)
  }

  override def undo(): Unit = {
    grid.removeCard(position._1, position._2)
    grid.setColor(position._1, position._2, previousColor)
    player.addPoints(-earnedPoints)
    // Restore the previous state
    grid.updateGrid(previousState.getGrid)
    player.updatePoints(previousState.getPoints)
    previousState.getPlayers.foreach(p => p.updateHand(previousState.getPlayers.head.getHand))
    previousState.getPlayers.foreach(p => p.updatePoints(previousState.getPoints))
  }

  override def redo(): Unit = {
    execute()
  }
}