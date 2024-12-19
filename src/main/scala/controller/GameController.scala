// src/main/scala/controller/GameController.scala
package controller

import model.*
import model.cards.NumberCards
import model.cards.Suit.Suit
import util.Observer
import util.grid.GridFactory

class GameController {
  private val deck = new Deck()
  private val grid = GridFactory.createGrid(3) // Use GridFactory to create a grid with random colors
  private var observer: Option[Observer] = None
  private val game = new Game(deck, grid)

  def setObserver(observer: Observer): Unit = {
    this.observer = Some(observer)
  }


  def startGame(): Unit = {
    val game = new Game(deck, grid)
    observer.foreach(game.add)
    game.start(player1, player2)
  }

  var player1: String = ""
  var player2: String = ""

  def promptForPlayerName(player1: String, player2: String) = {
    game.addPlayers(player1, player2)
    this.player1 = player1
    this.player2 = player2
  }

  def getGridColors: List[(Int, Int, Option[NumberCards], Suit)] = {
    grid.toArray.zipWithIndex.flatMap { case (row, x) =>
      row.zipWithIndex.map { case ((card, color), y) =>
        (x, y, card, color)
      }
    }.toList
  }
  
  def getCurrentplayer = game.getCurrentplayer
}