package model.objects.cards

import model.objects.{Grid, Player}
import model.objects.cards.AssistCardHandler

trait AssistCards extends Card {
    def name: String

    /**
     * Applies the effect of this card by delegating to the AssistCardHandler.
     * @param currentPlayer The player playing the card.
     * @param opponentPlayer The opponent player.
     * @param grid The current grid of the game.
     */
    def applyEffect(currentPlayer: Player, opponentPlayer: Player, grid: Grid): Unit = {
        AssistCardHandler.handleAssistCard(name, currentPlayer, opponentPlayer, grid)
    }

    override def toString: String = s"AssistCard($name)"
}