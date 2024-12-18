// src/main/scala/controller/GameController.scala
package controller

import model.*
import util.Observer

class GameController {
    private val deck = new Deck()
    private val grid = Grid()
    private var observer: Option[Observer] = None
    private val game = new Game(deck, grid)

    def setObserver(observer: Observer): Unit = {
        this.observer = Some(observer)
    }

    def startGame(): Unit = {
        val game = new Game(deck, grid)
        observer.foreach(game.add)
        game.start( player1, player2 )
    }
    var player1: String = ""
    var player2: String = ""

     def promptForPlayerName(player1 : String, player2 : String) = {
         game.addPlayers(player1, player2)
         this.player1 = player1
          this.player2 = player2


    }
}