package controller

import model.cardComp.CardInterface
import model.cardComp.baseImp.{Grid, NumberCards}
import model.deckComp.baseImp.Deck
import model.handComp.baseImp.Hand
import model.playerComp.baseImp.Player
import util.*
import util.command.{CommandManager, GameState, PlaceCardCommand}

import scala.util.{Failure, Success, Try}

class Game(deck: Deck, grid: Grid, controller: GameController) {
    private var currentPlayer: Player = _
    var player1: Player = _
    var player2: Player = _
    private val commandManager = new CommandManager()
    private var currentState: GameState = new GameState(grid, List(), 0, 0)
    private var hand: Hand = new Hand()

    def start(player1Name: String, player2Name: String): Unit = {

        val (p1, p2) = addPlayers(player1Name, player2Name)

        player1 = p1
        player2 = p2
        currentPlayer = player1
        controller.updateCurrentPlayer(currentPlayer)
        distributeInitialCards()
        controller.updateGrid(grid)
        controller.showCardsForPlayer(currentPlayer.getHand)

    }

    private def distributeInitialCards(): Unit = {
        for (_ <- 1 to 3) {
            player1.drawCard(deck)
            player2.drawCard(deck)
        }
    }

    def drawCardForCurrentPlayer(): Unit = {
        currentPlayer.drawCard(deck) match {
            case Some(card) =>
                controller.cardDrawn(currentPlayer.name, card.toString)
                controller.showCardsForPlayer(currentPlayer.getHand)
            case None =>
                controller.invalidPlacement()
        }
    }

    private def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean = {
        action() match {
            case Some(state) =>
                currentState = state
                controller.notifyUndoRedo(event)
                controller.showCardsForPlayer(currentPlayer.getHand)
                true
            case None =>
                false
        }
    }

    def handleCardPlacement(input: String): Boolean = {
        input.trim.toLowerCase match {
            case "undo" =>
                executeUndoRedo(commandManager.undo, UndoEvent(currentState))
            case "redo" =>
                executeUndoRedo(commandManager.redo, RedoEvent(currentState))
            case _ =>
                Try {
                    val parts = input.split(" ")
                    val cardIndex = parts(0).toInt
                    val x = parts(1).toInt
                    val y = parts(2).toInt

                    currentPlayer.getHand.lift(cardIndex) match {
                        case Some(card: NumberCards) =>
                            if (grid.placeCard(x, y, card)) {
                                val points = grid.calculatePoints(x, y)
                                val pointsEarned = grid.calculatePoints(x, y)
                                val command = new PlaceCardCommand(grid, card, currentPlayer, points, (x, y))
                                currentState = commandManager.executeCommand(command, currentState)
                                currentPlayer.addPoints(pointsEarned)
                                controller.cardPlacementSuccess(x, y, card.toString, pointsEarned)
                                currentPlayer.removeCard(card)
                                // grid.display() // Display updated grid
                                true
                            } else {
                                controller.invalidPlacement()
                                false
                            }
                        case _ =>
                            controller.invalidPlacement()
                            false
                    }
                } match {
                    case Success(result) => result
                    case Failure(_) =>
                        controller.invalidPlacement()
                        false
                }
        }
    }

    def getCurrentplayer: Player = currentPlayer

    def switchTurns(): Unit = {
        currentPlayer = if (currentPlayer == player1) player2 else player1
        controller.updateCurrentPlayer(currentPlayer)
        controller.updateGrid(grid)
        controller.showCardsForPlayer(currentPlayer.getHand)
    }


    def displayFinalScores(): Unit = {
        controller.gameOver(player1.name, player1.points, player2.name, player2.points)
    }

    private def addPlayers(player1Name: String, player2Name: String): (Player, Player) = {
        val player1 = Player(player1Name)
        val player2 = Player(player2Name)
        (player1, player2)
    }
}