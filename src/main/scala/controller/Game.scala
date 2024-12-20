// src/main/scala/model/Game.scala
package controller

import model.cards.{Card, Hand, NumberCards}
import model.{Deck, Grid, Player}
import util.*
import util.command.{CommandManager, GameState, PlaceCardCommand}

import scala.util.{Failure, Success, Try}


class Game(deck: Deck, grid: Grid, controller: GameController) {
  private var currentPlayer: Player = _
  var player1: Player = _
  var player2: Player = _
  private val commandManager = new CommandManager()
  private var currentState: GameState = new GameState(grid, List(), 0, 0) // Added points argument
  var hand: Hand = new Hand()
  private var observer: Option[Observer] = None


  def askForPlayerNames(): Unit = {
    controller.askForPlayerNames()
  }

  def start(player1Name: String, player2Name: String): Unit = {
    //controller.intro()

    val (p1, p2) = addPlayers(player1Name, player2Name)

    player1 = p1
    player2 = p2
    currentPlayer = player1

    //controller.updatePlayers(player1, player2)
    //controller.updateCurrentPlayer(currentPlayer)
    distributeInitialCards()
    controller.updateGrid(grid)
    gameLoop()
    displayFinalScores()
  }

  private def distributeInitialCards(): Unit = {
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }
    controller.showCardsForPlayer(player1.getHand)
  }

  private def gameLoop(): Unit = {
    while (deck.size > 0 && !grid.isFull) {
      controller.notifyPlayerTurn(currentPlayer.name)
      handlePlayerTurn()
      switchTurns()

    }
  }

  private def handlePlayerTurn(): Unit = {
    drawCardForCurrentPlayer()
    controller.waitForPlayerInput()
    processPlayerInput()
  }

  private def drawCardForCurrentPlayer(): Unit = {
    currentPlayer.drawCard(deck) match {
      case Some(card) =>
        controller.cardDrawn(currentPlayer.name, card.toString)
        controller.showCardsForPlayer(currentPlayer.getHand)
      case None =>
        controller.invalidPlacement()
    }
  }

  var input = ""

  def setInput(input: String): Unit = {
    this.input = input
  }

  private def processPlayerInput(): Unit = {
    var validInput = false
    while (!validInput) {
      if (input.trim.toLowerCase == "draw") {
        drawCardForCurrentPlayer()
        validInput = true
      } else {
        validInput = handleCardPlacement(input)
      }
    }
  }

  private def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean = {
    action() match {
      case Some(state) =>
        currentState = state
        controller.notifyUndoRedo(event)
        true
      case None =>
        false
    }
  }

  private def handleCardPlacement(input: String): Boolean = {
    input.trim.toLowerCase match {
      case "undo" =>
        executeUndoRedo(commandManager.undo, UndoEvent(currentState))
      case "redo" =>
        executeUndoRedo(commandManager.redo, RedoEvent(currentState))
      case _ =>
        Try {
          print(input + "------------------------------------")
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


  private def switchTurns(): Unit = {
    controller.updateGrid(grid)
    currentPlayer = if (currentPlayer == player1) player2 else player1
    print(s"Switching turns... $currentPlayer ")
    controller.updateCurrentPlayer(currentPlayer)


  }

  private def displayFinalScores(): Unit = {
    controller.gameOver(player1.name, player1.points, player2.name, player2.points)
  }

  def addPlayers(player1Name: String, player2Name: String): (Player, Player) = {
    val player1 = Player(player1Name)
    val player2 = Player(player2Name)
    (player1, player2)
  }
}