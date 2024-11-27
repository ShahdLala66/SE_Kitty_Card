// src/main/scala/controller/GameController.scala
package controller

import model.*
import util.{GameCallbacks, GameEvent, Observer}

class GameController extends GameCallbacks with Observer {
    private val deck = new Deck()
    private val grid = Grid()
    private var observer: Option[Observer] = None

    def setObserver(observer: Observer): Unit = {
        this.observer = Some(observer)
    }

    def startGame(): Unit = {
        val player1Name = promptForPlayerName("Player 1")
        val player2Name = promptForPlayerName("Player 2")

        val game = new Game(deck, grid)
        observer.foreach(game.add)
        game.start(player1Name, player2Name)
    }

    def promptForPlayerName(player: String): String = {
        println(s"Enter the name for $player:")
        val name = scala.io.StdIn.readLine()
        if (name == null) "Anonym" else name
    }

    override def update(event: GameEvent): Unit = {
        observer.foreach(_.update(event))
    }

    def displayBadChoice(color: String): Unit = {
        println(s"Bad choice: $color")
    }

    def displayCatInColor(color: String): Unit = {
        println(s"Cat in color: $color")
    }

    def displayMeh(color: String): Unit = {
        println(s"Meh: $color")
    }
}