package controller

import model.*
import model.cards.NumberCards
import model.cards.Suit.Suit
import util.grid.GridFactory
import util.{Observable, Observer, PlayerTurn, PromptForPlayerName}

import java.time.InstantSource.system

class GameController {
  private val deck = new Deck()
  private val grid = GridFactory.createGrid(3)
  private var observer: Option[Observer] = None
  private val game = new Game(deck, grid)
  private var gameEndCallback: () => Unit = () => {}

  var player1: String = ""
  var player2: String = ""
  var counter = 0

  def setObserver(observer: Observer): Unit = {
    this.observer = Some(observer)
    //game.add(observer)
  }

  def startGame(): Unit = {
    observer.foreach(game.add)
    game.askForPlayerNames()
    game.start(player1, player2)
    startGameLoop()
  }

  def startGameLoop(): Unit = {
    while (!isGameOver) {
      if (game.getCurrentplayer != null) {
        observer.foreach(_.update(PlayerTurn(game.getCurrentplayer.name)))
      }
    }
    game.displayFinalScores()
    System.exit(0)
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
    }
  }

  def promptForPlayerName(player1: String, player2: String): Unit = {
    if (counter == 0) {
      this.player1 = player1
      this.player2 = player2
      counter += 1
      //startGame()
    } else {
      print("Game already started")
    }
  }

  def getGridColors: List[(Int, Int, Option[NumberCards], Suit)] = {
    grid.toArray.zipWithIndex.flatMap { case (row, x) =>
      row.zipWithIndex.map { case ((card, color), y) =>
        (x, y, card, color)
      }
    }.toList
  }

  private def isGameOver: Boolean = {
    deck.size <= 0 || grid.isFull
  }

  def getCurrentplayer = game.getCurrentplayer

  def getGridColor(x: Int, y: Int): String = {
    grid.toArray(x)(y)._2.toString.toLowerCase
  }
}
