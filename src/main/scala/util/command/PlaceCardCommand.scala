package util.command

import model.baseImp.{Grid, NumberCards, Player}

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
        player.points = previousState.getPlayers.head.points
       // player.updateHand(previousState.getPlayers.head.getHand) ok with this one it just slurps the cards haha lol
        //the issue of the cards is here please fix it ahhh
        player.addCard(card) //lol das war das problen sogar ohne copilot lol
    }
//hujkl
    override def redo(): Unit = {
        execute()
    }
}