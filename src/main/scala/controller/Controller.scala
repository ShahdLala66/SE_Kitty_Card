package controller

import model._
import view.CatPrint
import util.ErrorMessages

class GameController {
  private val deck = new Deck()
  private val grid = new Grid(3)
  private val catPrint = new CatPrint() // Create CatPrint instance

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

          var validInput = false
          while (!validInput) {
            try {
              // Ask the player where to place the card
              println("Enter the x and y coordinates (e.g., 0 1) to place the card:")
              val input = scala.io.StdIn.readLine().split(" ").map(_.toInt)
              val (x, y) = (input(0), input(1))

              // Try placing the card
              if (grid.placeCard(x, y, card)) {
                validInput = true

                // Fetch the color (suit) of the rectangle at (x, y)
                val rectangleColor = grid.getRectangleColors(x, y)

                // Display the appropriate cat based on color matching
                if (rectangleColor == Suit.White) {
                  catPrint.printMeh(card.suit.toString) // Print "meh" cat if on white
                } else if (rectangleColor == card.suit) {
                  catPrint.printCatInColor(card.suit.toString) // Print matching color cat
                } else {
                  catPrint.printBadChoice(rectangleColor.toString) // Print "bad choice" cat if mismatch
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
            } catch {
              case _: NumberFormatException =>
                println(ErrorMessages.getRandomMessage)
              case _: ArrayIndexOutOfBoundsException =>
                println(ErrorMessages.getRandomMessage)
            }
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