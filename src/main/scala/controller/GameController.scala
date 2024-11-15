// src/main/scala/controller/GameController.scala
package controller

import model._
import util.{ErrorMessages, GameCallbacks, Observer, GameEvent}

class GameController extends GameCallbacks with Observer {
    private val deck = new Deck()
    private val grid = Grid()
    private var observer: Option[Observer] = None

    def setObserver(observer: Observer): Unit = {
        this.observer = Some(observer)
    }

    def startGame(): Unit = {
        println("Press Enter to start the game.")
        val familyFriendly = scala.io.StdIn.readLine().trim.toLowerCase == " "
        ErrorMessages.loadMessages(familyFriendly)

        println("Enter the name for Player 1:")
        val player1Name = scala.io.StdIn.readLine()
        val player1 = Player(player1Name)
        println("Enter the name for Player 2:")
        val player2Name = scala.io.StdIn.readLine()
        val player2 = Player(player2Name)

        val game = new Game(player1, player2, deck, grid)
        observer.foreach(game.add)
        game.start()
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