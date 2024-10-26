import model._

import scala.io.StdIn.readLine

object KittyCardGame {

    def main(args: Array[String]): Unit = {
        val deck = new Deck()
        val grid = new Grid(3)
        val player1 = Player("You 1")
        val player2 = Player("Zayne, your dom. 2")

        println("Welcome to the Kitty Card Game!")
        println("Players take turns drawing and placing cards on the grid.")
        println("Earn points by placing cards on matching colors or white squares.")
        println()

        // Display the initial grid with random rectangle colors
        grid.displayInitialColors()

        var currentPlayer = player1

        // Game loop: Continue until the deck has drawn the maximum number of cards or the grid is full.
        while (deck.size > 0 && !grid.isFull()) {
            println(s"${currentPlayer.name}'s turn.")

            // Draw a card for the current player
            val drawnCard = deck.drawCard()
            drawnCard match {
                case Some(card) =>
                    println(s"${currentPlayer.name} drew: ${card.suit}, ${Value1.toInt(card.value)}")

                    // Ask the player where to place the card
                    println("Enter the x and y coordinates (e.g., 0 1) to place the card:")
                    val input = readLine().split(" ").map(_.toInt)
                    val (x, y) = (input(0), input(1))

                    // Try placing the card
                    if (grid.placeCard(x, y, card)) {
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

                case None =>
                    println("The deck is empty, no more cards can be drawn.")
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
}
