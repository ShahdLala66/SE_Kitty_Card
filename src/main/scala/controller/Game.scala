// src/main/scala/model/Game.scala
package controller

import model.cards.{Card, Hand, NumberCards}
import model.{Deck, Grid, Player}
import util.*
import util.command.{CommandManager, GameState, PlaceCardCommand}

import scala.util.{Failure, Success, Try}


class Game(deck: Deck, grid: Grid) extends Observable {
  var currentPlayer: Player = _
  var player1: Player = _
  var player2: Player = _
  private val commandManager = new CommandManager()
  private var currentState: GameState = new GameState(grid, List(), 0, 0) // Added points argument
  private var hand: Hand = new Hand()


  def askForPlayerNames(): Unit = {
    notifyObservers(PromptForPlayerName)
  }

  def start(player1Name: String, player2Name: String): Unit = {
    println("Starting game...")
    val (p1, p2) = addPlayers(player1Name, player2Name)
    println(s"Player 1: $p1")
    println(s"Player 2: $p2")

    player1 = p1
    player2 = p2
    currentPlayer = player1
    notifyObservers(UpdatePlayers(player1, player2))
    notifyObservers(UpdatePlayer(currentPlayer))
    // maybe problem
    // notifyObservers(PromptForPlayerName(player1Name, player2Name))
    distributeInitialCards()
    notifyObservers(updateGrid(grid))
    //grid.displayInitialColors()
    gameLoop()
    displayFinalScores()

  }

  def distributeInitialCards(): Unit = {
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }
      notifyObservers(ShowCardsForPlayer(player1.getHand))
  }

  def gameLoop(): Unit = {
    while (deck.size > 0 && !grid.isFull) {
      notifyObservers(PlayerTurn(currentPlayer.name))
      // currentPlayer.getHand.foreach(println) // Display cards in hand
      handlePlayerTurn()
      switchTurns()

    }
  }

  def handlePlayerTurn(): Unit = {
    drawCardForCurrentPlayer()
    notifyObservers(WaitForPlayerInput)
    processPlayerInput()
  }

  def drawCardForCurrentPlayer(): Unit = {
    currentPlayer.drawCard(deck) match {
      case Some(card) =>
        notifyObservers(CardDrawn(currentPlayer.name, card.toString))
        //currentPlayer.getHand.foreach(println) // Display updated hand
        notifyObservers(ShowCardsForPlayer(currentPlayer.getHand))
      case None =>
        notifyObservers(InvalidPlacement)
    }
  }

  var input = ""

  def setInput(input: String): Unit = {
    this.input = input
  }

  def processPlayerInput(): Unit = {
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

  def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean = {
    action() match {
      case Some(state) =>
        currentState = state
        notifyObservers(event)
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
                notifyObservers(CardPlacementSuccess(x, y, card.toString, pointsEarned))
                // grid.display() // Display updated grid
                true
              } else {
                notifyObservers(InvalidPlacement)
                false
              }
            case _ =>
              notifyObservers(InvalidPlacement)
              false
          }
        } match {
          case Success(result) => result
          case Failure(_) =>
            notifyObservers(InvalidPlacement)
            false
        }
    }
  }

  def getCurrentplayer: Player = currentPlayer


  def switchTurns(): Unit = {
    notifyObservers(updateGrid(grid))
    currentPlayer = if (currentPlayer == player1) player2 else player1
    print(s"Switching turns... $currentPlayer ")

  }

  def displayFinalScores(): Unit = {
    notifyObservers(GameOver(player1.name, player1.points, player2.name, player2.points))
  }

  def addPlayers(player1Name: String, player2Name: String): (Player, Player) = {
    val player1 = Player(player1Name, 0)
    val player2 = Player(player2Name, 0)
    (player1, player2)
  }
}