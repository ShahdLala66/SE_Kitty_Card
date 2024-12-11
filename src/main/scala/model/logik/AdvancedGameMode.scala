package model.logik

import logik.GameModeL
import model.objects.cards.NumberCards
import model.objects.{Deck, Grid, Player}
import model.objects.cards.AssistCards

import scala.util.{Success, Try}

class AdvancedGameMode(protected val grid: Grid, protected val deck: Deck) extends GameModeL(grid, deck) {

  override def processPlayerTurn(currentPlayer: Player, opponentPlayer: Player): Unit = {
    currentPlayer.drawCard(deck) // Draw card at the start of the turn
    println(s"${currentPlayer.name}'s turn. Hand: ${currentPlayer.getHand}")

    val input = receivePlayerInput() // Example: "cardIndex x y"

    Try {
      val cardIndex = input.split(" ")(0).toInt
      currentPlayer.getHand.lift(cardIndex)
    } match {
      case Success(Some(card: NumberCards)) =>
        if (!handleCardPlacement(currentPlayer, input)) println("Invalid move! Try again.")

      case Success(Some(assistCard: AssistCards)) =>
        println(s"Using Assist Card: ${assistCard.name}")
        assistCard.applyEffect(currentPlayer, opponentPlayer, grid)
        currentPlayer.removeCard(assistCard) // Card is used and removed

      case _ =>
        println("Invalid input or unsupported card type.")
    }
  }

  private def receivePlayerInput(): String = "0 1 1" // Placeholder for input
}