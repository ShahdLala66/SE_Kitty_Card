// src/main/scala/model/Game.scala
package model

import model.cards.{ NumberCards, Suit, Value}
import util.ErrorMessages
import view.CatPrint

class Game(player1: Player, player2: Player, deck: Deck, grid: Grid, catPrint: CatPrint) {
  private var currentPlayer = player1

  def start(): Unit = {
    println("Welcome to the Kitty Card Game!")
    println("Players take turns drawing and placing cards on the grid.")
    println("Earn points by placing cards on matching colors or white squares.")
    println()

    // Distribute three cards to each player at the start
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }

    // Display the initial grid with random rectangle colors
    grid.displayInitialColors()

    // Game loop: Continue until the deck has drawn the maximum number of cards or the grid is full.
    while (deck.size > 0 && !grid.isFull) {
      println(s"${currentPlayer.name}'s turn.")
      currentPlayer.getHand.displayCards()

      // Draw a card for the current player
      val drawnCard = currentPlayer.drawCard(deck)
      drawnCard match {
        case Some(card) =>
          card match {
            case numberCard: NumberCards =>
              println(s"${currentPlayer.name} drew: ${numberCard.suit}, ${Value.toInt(numberCard.value)}")
              currentPlayer.getHand.displayCards() // Display updated hand
            case _ =>
              println("Drew an unknown type of card")
          }
          var validInput = false
          while (!validInput) {
            val input = scala.io.StdIn.readLine("Enter the card index, x and y coordinates (e.g., 0 1 2) to place the card or type 'draw' to draw another card and end your turn:")
            if (input.trim.toLowerCase == "draw") {
              currentPlayer.drawCard(deck)
              println(s"${currentPlayer.name} drew another card and ended their turn.")
              currentPlayer.getHand.displayCards() // Display updated hand
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