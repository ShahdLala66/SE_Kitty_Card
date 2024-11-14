// src/main/scala/controller/GameController.scala
package controller

import model._
import model.Deck
import util.ErrorMessages
import view.Tui

class GameController {
    private val deck = new Deck()
    private val grid = Grid()
    private val catPrint = new Tui() // Create CatPrint instance

    def startGame(): Unit = {
        catPrint.printCatLoop()
        println("Press Enter to start the game.")
        val familyFriendly = scala.io.StdIn.readLine().trim.toLowerCase == " "
        ErrorMessages.loadMessages(familyFriendly)

        // Prompt for player names
        println("Enter the name for Player 1:")
        val player1Name = scala.io.StdIn.readLine()
        val player1 = Player(player1Name)
        println("Enter the name for Player 2:")
        val player2Name = scala.io.StdIn.readLine()
        val player2 = Player(player2Name)

        val game = new Game(player1, player2, deck, grid)
        game.start(this)
    }

    def displayCatInColor(color: String): Unit = {
        catPrint.printCatInColor(color)
    }

    def displayBadChoice(color: String): Unit = {
        catPrint.printBadChoice(color)
    }

    def displayMeh(color: String): Unit = {
        catPrint.printMeh(color)
    }
}