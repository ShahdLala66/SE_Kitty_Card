package util.command

import model.cards.NumberCards
import model.Grid
import model.Player

class

PlaceCardCommand(grid: Grid, card: NumberCards, player: Player, points: Int, position: (Int, Int)) extends CommandTrait {
    private val previousCard = grid.getCard(position._1, position._2)
    private val previousColor = grid.getColor(position._1, position._2)

    override def execute(): Unit = {
        previousState = new GameState(grid, List(player.copy()), 0, player.points) // Save the previous state
        grid.placeCard(position._1, position._2, card)
    }

    override def undo(): Unit = {
        grid.removeCard(position._1, position._2)
        grid.setColor(position._1, position._2, previousColor)
        // Restore the previous state
        grid.updateGrid(previousState.getGrid)
        previousState.getPlayers.foreach(p => p.updateHand(previousState.getPlayers.head.getHand))
    }

    override def redo(): Unit = {
        execute()
    }
}