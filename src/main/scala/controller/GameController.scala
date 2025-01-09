package controller

import model.*
import model.cardComp.CardInterface
import model.cardComp.baseImp.Grid
import model.cardComp.baseImp.Suit.Suit
import model.deckComp.baseImp.Deck
import model.playerComp.baseImp.Player
import util.*
import util.grid.GridFactory


class GameController extends Observable with GameControllerInterface {
  private val deck = new Deck()
  private val grid = GridFactory.createGrid(3)
  private var observers: List[Observer] = List()
  private val game = new Game(deck, grid, this)
  private var playerIsAtTurn = true


  var player1: String = ""
  var player2: String = ""
  var counter = 0


  def startGame(): Unit = {
    notifyObservers(PromptForPlayerName)

    val player1Obj = new Player(player1)
    val player2Obj = new Player(player2)
    notifyObservers(UpdatePlayers(player1Obj, player2Obj)) // update players

    game.start(player1, player2)

    startGameLoop()
  }

  def startGameLoop(): Unit = {
    while (playerIsAtTurn) {
      if (game.getCurrentplayer != null) {
        playerIsAtTurn = false // Stop loop after getting current player
        notifyObservers(PlayerTurn(game.getCurrentplayer.name))
      }
    }
  }


  def handleCommand(command: String): Unit = {
    command match {
      case "undo" => game.handleCardPlacement("undo")
      case "redo" => game.handleCardPlacement("redo")
      case "draw" => game.drawCardForCurrentPlayer()
    }
  }

  def handleCardPlacement(cardIndex: Int, x: Int, y: Int): Unit = {
    if (game.handleCardPlacement(s"$cardIndex $x $y")) {
      game.switchTurns()
      playerIsAtTurn = true // Resume loop for next turn
    }
    if (!isGameOver) {
      playerIsAtTurn = true // Reset flag if game isn't over
    } else {
      game.displayFinalScores()
      System.exit(0)
    }
  }

  def promptForPlayerName(player1: String, player2: String): Unit = {
    if (counter == 0) {
      this.player1 = player1
      this.player2 = player2
      counter += 1
      notifyObservers(UpdatePlayers(new Player(player1), new Player(player2)))
    } else {
      println("Game already started")
    }
  }

  def getGridColors: List[(Int, Int, Option[CardInterface], Suit)] = {
    grid.toArray.zipWithIndex.flatMap { case (row, x) =>
      row.zipWithIndex.map { case ((card, color), y) =>
        (x, y, card, color)
      }
    }.toList
  }

  def isGameOver: Boolean = {
    deck.size <= 0 || grid.isFull
  }

  def getCurrentplayer = game.getCurrentplayer

  def getGridColor(x: Int, y: Int): String = {
    grid.toArray(x)(y)._2.toString.toLowerCase
  }

  def updatePlayers(player1: Player, player2: Player): Unit =
    notifyObservers(UpdatePlayers(player1, player2))

  def updateCurrentPlayer(player: Player): Unit =
    notifyObservers(UpdatePlayer(player.name))

  def showCardsForPlayer(hand: List[CardInterface]): Unit =
    notifyObservers(ShowCardsForPlayer(hand))

  def notifyPlayerTurn(playerName: String): Unit = notifyObservers(PlayerTurn(playerName))

  def cardDrawn(playerName: String, card: String): Unit =
    notifyObservers(CardDrawn(playerName, card))

  def invalidPlacement(): Unit =
    notifyObservers(InvalidPlacement)

  def notifyUndoRedo(event: GameEvent): Unit =
    notifyObservers(event)

  def cardPlacementSuccess(x: Int, y: Int, card: String, points: Int): Unit =
    notifyObservers(CardPlacementSuccess(x, y, card, points))

  def updateGrid(grid: Grid): Unit =
    notifyObservers(UpdateGrid(grid))

  def gameOver(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int): Unit =
    notifyObservers(GameOver(player1Name, player1Points, player2Name, player2Points))

  // def intro(): Unit = {
  //  notifyObservers(GameStart)
  //}
}