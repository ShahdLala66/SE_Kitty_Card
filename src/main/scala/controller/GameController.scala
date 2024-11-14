// src/main/scala/controller/GameController.scala
package controller

import model._
import model.Deck
import util.ErrorMessages
import view.CatPrint

class GameController {
    private val deck = new Deck()
    private val grid = new Grid()
    private val catPrint = new CatPrint() // Create CatPrint instance

    def startGame(): Unit = {
        catPrint.printCatLoop()
        println("Do you want the family-friendly version? (y/n):")
        val familyFriendly = scala.io.StdIn.readLine().trim.toLowerCase == "y"
        ErrorMessages.loadMessages(familyFriendly)

        // Prompt for player names
        println("Enter the name for Player 1:")
        val player1Name = scala.io.StdIn.readLine()
        val player1 = Player(player1Name)
        println("Enter the name for Player 2:")
        val player2Name = scala.io.StdIn.readLine()
        val player2 = Player(player2Name)

        val game = new Game(player1, player2, deck, grid, catPrint)
        game.start()
    }
}