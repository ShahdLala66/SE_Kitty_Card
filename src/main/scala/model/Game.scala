package model

import model.cards.{NumberCards, Suit, Value}
import util.{ErrorMessages, GameCallbacks, Observable}


class Game(player1: Player, player2: Player, deck: Deck, grid: Grid) extends Observable {
  var currentPlayer: Player = player1

  def start(callbacks: GameCallbacks): Unit = {
    add(callbacks)
    welcomeMessage()
    distributeInitialCards()
    grid.displayInitialColors()
    gameLoop()
    displayFinalScores()
  }

  def distributeInitialCards(): Unit = {
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }
  }

  private def gameLoop(): Unit = {
    while (deck.size > 0 && !grid.isFull) {
      println(Console.BLUE + s"\n${currentPlayer.name}'s turn.\n" + Console.RESET)
      currentPlayer.getHand.displayCards()
      handlePlayerTurn()
      switchTurns()
    }
  }

  def welcomeMessage(): Unit = {
    println(Console.MAGENTA + "\nWelcome to the Kitty Card Game!")
    println("Players take turns drawing and placing cards on the grid.")
    println("Earn points by placing cards on matching colors or white squares." + Console.RESET)
    println()
  }

  private def handlePlayerTurn(): Unit = {
    currentPlayer.drawCard(deck) match {
      case Some(card) =>
        card match {
          case numberCard: NumberCards =>
            println(Console.BLUE + s"\n${currentPlayer.name} drew: ${numberCard.suit}, ${Value.toInt(numberCard.value)}\n" + Console.RESET)
            currentPlayer.getHand.displayCards()
          case _ =>
            println("Drew an unknown type of card")
        }
        processPlayerInput()
      case None =>
        println("Deck is empty :c")
    }
  }

  def processPlayerInput(): Unit = {
    var validInput = false
    while (!validInput) {
      val input = scala.io.StdIn.readLine(Console.YELLOW + "Enter the card index, x and y coordinates (e.g., 0 1 2)" +
          " to place the card or type 'draw' to draw another card and end your turn:\n" + Console.RESET)
      if (input.trim.toLowerCase == "draw") {
        currentPlayer.drawCard(deck)
        println(s"${currentPlayer.name} drew another card and ended their turn.")
        currentPlayer.getHand.displayCards()
        validInput = true
      } else {
        validInput = handleCardPlacement(input)
      }
    }
  }

  def handleCardPlacement(input: String): Boolean = {
    try {
      val parts = input.split(" ")
      val cardIndex = parts(0).toInt
      val x = parts(1).toInt
      val y = parts(2).toInt

      currentPlayer.getHand.getCard(cardIndex) match {
        case Some(card: NumberCards) =>
          if (grid.placeCard(x, y, card)) {
            handleCardPlacementSuccess(x, y, card)
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

  private def handleCardPlacementSuccess(x: Int, y: Int, card: NumberCards): Unit = {
    val rectangleColor = grid.getRectangleColors(x, y)
    if (rectangleColor == Suit.White) {
      notifyObservers(_.displayMeh(card.suit.toString))
    } else if (rectangleColor == card.suit) {
      notifyObservers(_.displayCatInColor(card.suit.toString))
    } else {
      notifyObservers(_.displayBadChoice(rectangleColor.toString))
    }
    val pointsEarned = grid.calculatePoints(x, y)
    currentPlayer.addPoints(pointsEarned)
    println(Console.YELLOW + s"${currentPlayer.name} earned $pointsEarned points." + Console.RESET)
    println("\nUpdated Grid:")
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

  private def notifyObservers(action: GameCallbacks => Unit): Unit = {
    subscribers.foreach {
      case callback: GameCallbacks => action(callback)
      case _ =>
    }
  }
}