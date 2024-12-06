// src/main/scala/model/Game.scala
package model

import model.cards.{Card, NumberCards}
import model.patterns.GameMode
import util.*
import util.command.{CommandManager, PlaceCardCommand}

import scala.util.{Failure, Success, Try}

class Game(deck: Deck, grid: Grid, var gameMode: GameMode) extends Observable {
  var currentPlayer: Player = _
  var player1: Player = _
  var player2: Player = _
  private val commandManager = new CommandManager()
  private var currentState: GameState = new GameState(grid, List(), 0, 0) // Added points argument

  
  def isGridFull: Boolean = grid.isFull

  def getGrid: Grid = grid

  def initialize(): Unit = {
    //grid.displayInitialColors()
  }

  def start(player1Name: String, player2Name: String): Unit = {
    val (p1, p2) = addPlayers(player1Name, player2Name)
    player1 = p1
    player2 = p2
    currentPlayer = player1
    distributeInitialCards()
    grid.displayInitialColors()
    gameMode.startGame()
    //displayFinalScores()
  }

  private def distributeInitialCards(): Unit = {
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }
  }

  def playTurn(): Unit = {
    gameMode.playTurn()
  }

  def handlePlayerTurn(): Unit = {
    drawCardForCurrentPlayer()
    processPlayerInput()
  }

  def drawCardForCurrentPlayer(): Unit = {
    currentPlayer.drawCard(deck) match {
      case Some(card) =>
        notifyObservers(CardDrawn(currentPlayer.name, card.toString))
        currentPlayer.getHand.foreach(println)
      case None =>
        notifyObservers(InvalidPlacement())
    }
  }

  def processPlayerInput(): Unit = {
    var validInput = false
    while (!validInput) {
      val input = scala.io.StdIn.readLine()
      input.trim.toLowerCase match {
        case "undo" =>
          commandManager.undo().foreach { state =>
            currentState = state
            notifyObservers(UndoEvent(currentState))
          }
          validInput = true
        case "redo" =>
          commandManager.redo().foreach { state =>
            currentState = state
            notifyObservers(RedoEvent(currentState))
          }
          validInput = true
        case "draw" =>
          drawCardForCurrentPlayer()
          validInput = true
        case _ =>
          validInput = handleCardPlacement(input)
      }
    }
  }

  private def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean = {
    action().foreach { state =>
      currentState = state
      notifyObservers(event)
    }
    true
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
              val points = grid.calculatePoints(x, y)
              val command = new PlaceCardCommand(grid, card, currentPlayer, points, (x, y))
              currentState = commandManager.executeCommand(command, currentState)
              currentPlayer.addPoints(points)
              notifyObservers(CardPlacementSuccess(x, y, card.toString, points))
              notifyObservers(TotalPoints(player1.points, player2.points))
              true
            case _ =>
              notifyObservers(InvalidPlacement())
              false
          }
        } match {
          case Success(result) => result
          case Failure(_) =>
            notifyObservers(InvalidPlacement())
            false
        }
    }
  }
  

  def switchTurns(): Unit = {
    grid.display()
    currentPlayer = if (currentPlayer == player1) player2 else player1
    notifyObservers(PlayerTurn(currentPlayer.name))
  }

  private def displayFinalScores(): Unit = {
    notifyObservers(GameOver(player1.name, player1.points, player2.name, player2.points))
  }

  private def addPlayers(player1Name: String, player2Name: String): (Player, Player) = {
    val player1 = Player(player1Name)
    val player2 = Player(player2Name)
    (player1, player2)
  }
}