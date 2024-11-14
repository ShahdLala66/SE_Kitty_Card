// src/main/scala/model/Game.scala
package model

import model.cards.{NumberCards, Suit, Value}
import util.ErrorMessages
import controller.GameController

class Game(player1: Player, player2: Player, deck: Deck, grid: Grid) {
  var currentPlayer: Player = player1

  def start(controller: GameController): Unit = {
    welcomeMessage()
    distributeInitialCards()
    grid.displayInitialColors()
    gameLoop(controller)
    displayFinalScores()
  }

  def welcomeMessage(): Unit = {
    println("Welcome to the Kitty Card Game!")
    println("Players take turns drawing and placing cards on the grid.")
    println("Earn points by placing cards on matching colors or white squares.")
    println()
  }

  def distributeInitialCards(): Unit = {
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }
  }

  private def gameLoop(controller: GameController): Unit = {
    while (deck.size > 0 && !grid.isFull) {
      println(s"${currentPlayer.name}'s turn.")
      currentPlayer.getHand.displayCards()
      handlePlayerTurn(controller)
      switchTurns()
    }
  }

  private def handlePlayerTurn(controller: GameController): Unit = {
    currentPlayer.drawCard(deck) match {
      case Some(card) =>
        card match {
          case numberCard: NumberCards =>
            println(s"${currentPlayer.name} drew: ${numberCard.suit}, ${Value.toInt(numberCard.value)}")
            currentPlayer.getHand.displayCards()
          case _ =>
            println("Drew an unknown type of card")
        }
        processPlayerInput(controller)
      case None =>
        println("Deck is empty :c")
    }
  }

  def processPlayerInput(controller: GameController): Unit = {
    var validInput = false
    while (!validInput) {
      val input = scala.io.StdIn.readLine("Enter the card index, x and y coordinates (e.g., 0 1 2) to place the card or type 'draw' to draw another card and end your turn:")
      if (input.trim.toLowerCase == "draw") {
        currentPlayer.drawCard(deck)
        println(s"${currentPlayer.name} drew another card and ended their turn.")
        currentPlayer.getHand.displayCards()
        validInput = true
      } else {
        validInput = handleCardPlacement(input, controller)
      }
    }
  }

  def handleCardPlacement(input: String, controller: GameController): Boolean = {
    try {
      val parts = input.split(" ")
      val cardIndex = parts(0).toInt
      val x = parts(1).toInt
      val y = parts(2).toInt

      currentPlayer.getHand.getCard(cardIndex) match {
        case Some(card: NumberCards) =>
          if (grid.placeCard(x, y, card)) {
            handleCardPlacementSuccess(x, y, card, controller)
            true
          } else {
            println("Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.")
            false
          }
        case _ =>
          println("Invalid card index")
          false
      }
    } catch {
      case _: NumberFormatException =>
        printErrorMessage(input)
        false
      case _: ArrayIndexOutOfBoundsException =>
        printErrorMessage(input)
        false
    }
  }

  private def handleCardPlacementSuccess(x: Int, y: Int, card: NumberCards, controller: GameController): Unit = {
    val rectangleColor = grid.getRectangleColors(x, y)
    if (rectangleColor == Suit.White) {
      controller.displayMeh(card.suit.toString)
    } else if (rectangleColor == card.suit) {
      controller.displayCatInColor(card.suit.toString)
    } else {
      controller.displayBadChoice(rectangleColor.toString)
    }
    val pointsEarned = grid.calculatePoints(x, y)
    currentPlayer.addPoints(pointsEarned)
    println(s"${currentPlayer.name} earned $pointsEarned points.")
    println("Updated Grid:")
    grid.display()
  }

  def switchTurns(): Unit = {
    currentPlayer = if (currentPlayer == player1) player2 else player1
  }

  def displayFinalScores(): Unit = {
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