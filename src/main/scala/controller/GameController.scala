package controller

import model.*
import model.cards.{NumberCards, Suit, Value}
import util.ErrorMessages
import view.CatPrint

class GameController {
    private val deck = new Deck()
    private val grid = Grid()
    private val catPrint = new CatPrint() // Create CatPrint instance

    def getDeck: Deck = deck

    def getGrid: Grid = grid

    def getCatPrint: CatPrint = catPrint

    def startGame(): Unit = {
        println("Welcome to the Kitty Card Game!")
        catPrint.printCatLoop()
        println("Players take turns drawing and placing cards on the grid.")
        println("Earn points by placing cards on matching colors or white squares.")
        println()

        // Prompt for family-friendly version
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

        // Distribute three cards to each player at the start
        for (_ <- 1 to 3) {
            player1.drawCard(deck)
            player2.drawCard(deck)
        }

        // Display the initial grid with random rectangle colors
        grid.displayInitialColors()

        var currentPlayer = player1

        // Game loop: Continue until the deck has drawn the maximum number of cards or the grid is full.
        while (deck.size > 0 && !grid.isFull) {
            println(s"${currentPlayer.name}'s turn.")
            currentPlayer.getHand.displayCards()

            // Draw a card for the current player
            val drawnCard = deck.drawCard()
            drawnCard match {
                case Some(card) =>
                    println(s"${currentPlayer.name} drew: ${card.suit}, ${Value.toInt(card.value)}")

                    var validInput = false
                    while (!validInput) {
                        val input = scala.io.StdIn.readLine("Enter the card index, x and y coordinates (e.g., 0 1 2) to place the card or type 'draw' to draw another card and end your turn:")
                        if (input.trim.toLowerCase == "draw") {
                            currentPlayer.drawCard(deck)
                            println(s"${currentPlayer.name} drew another card and ended their turn.")
                            currentPlayer.getHand.displayCards()
                            validInput = true
                        } else {
                            try {
                                val parts = input.split(" ")
                                val cardIndex = parts(0).toInt
                                val x = parts(1).toInt
                                val y = parts(2).toInt

                                currentPlayer.getHand.getCard(cardIndex) match {
                                    case Some(card: NumberCards) =>
                                        if (grid.placeCard(x, y, card)) {
                                            validInput = true

                                            // Fetch the color (suit) of the rectangle at (x, y)
                                            val rectangleColor = grid.getRectangleColors(x, y)

                                            // Display the appropriate cat based on color matching
                                            if (rectangleColor == Suit.White) {
                                                catPrint.printMeh(card.suit.toString)
                                            } else if (rectangleColor == card.suit) {
                                                catPrint.printCatInColor(card.suit.toString)
                                            } else {
                                                catPrint.printBadChoice(rectangleColor.toString)
                                            }

                                            // Calculate points and update the player's score
                                            val pointsEarned = grid.calculatePoints(x, y)
                                            currentPlayer.addPoints(pointsEarned)
                                            println(s"${currentPlayer.name} earned $pointsEarned points.")

                                            // Display the updated grid
                                            println("Updated Grid:")
                                            grid.display()
                                        } else {
                                            println("Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.")
                                        }
                                    case _ => println("Invalid card index")
                                }
                            } catch {
                                case _: NumberFormatException => printErrorMessage(input)
                                case _: ArrayIndexOutOfBoundsException => printErrorMessage(input)
                            }
                        }
                    }
                case None =>
                    println("Deck is empty :c")
            }
            // Switch turns between players
            currentPlayer = if (currentPlayer == player1) player2 else player1
        }

        // Game ends, display the winner
        println("Game over!")
        println(s"${player1.name}'s final score: ${player1.points}")
        println(s"${player2.name}'s final score: ${player2.points}")

        if (player1.points > player2.points) {
            println(s"${player1.name} wins!")
        } else if (player2.points > player1.points) {
            println(s"${player2.name} wins!")
        } else {
            println("It's a tie!")
        }
    }

    private def printErrorMessage(input: String): Unit = {
        ErrorMessages.getSpecificMessage(input) match {
            case Some(message) => println(message)
            case None => println(ErrorMessages.getRandomMessage)
        }
    }
}