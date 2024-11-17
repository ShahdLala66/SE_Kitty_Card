// src/main/scala/model/Game.scala
package model

import model.cards.{Card, NumberCards}
import util.*

import scala.util.{Failure, Success, Try}

class Game(player1: Player, player2: Player, deck: Deck, grid: Grid) extends Observable {
  var currentPlayer: Player = player1

  def start(): Unit = {
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

  def gameLoop(): Unit = {
    while (deck.size > 0 && !grid.isFull) {
      notifyObservers(PlayerTurn(currentPlayer.name))
      currentPlayer.getHand.getCards.foreach(println) // Display cards in hand
      handlePlayerTurn()
      switchTurns()
    }
  }

  def handlePlayerTurn(): Unit = {
    drawCardForCurrentPlayer()
    processPlayerInput()
  }

  def drawCardForCurrentPlayer(): Unit = {
    currentPlayer.drawCard(deck) match {
      case Some(card) =>
        notifyObservers(CardDrawn(currentPlayer.name, card.toString))
        currentPlayer.getHand.getCards.foreach(println) // Display updated hand
      case None =>
        notifyObservers(InvalidPlacement())
    }
  }

  def processPlayerInput(): Unit = {
    var validInput = false
    while (!validInput) {
      val input = scala.io.StdIn.readLine()
      if (input.trim.toLowerCase == "draw") {
        drawCardForCurrentPlayer()
        validInput = true
      } else {
        validInput = handleCardPlacement(input)
      }
    }
  }

  def handleCardPlacement(input: String): Boolean = {
    Try {
      val parts = input.split(" ")
      val cardIndex = parts(0).toInt
      val x = parts(1).toInt
      val y = parts(2).toInt

      currentPlayer.getHand.getCards.lift(cardIndex) match {
        case Some(card: NumberCards) =>
          if (grid.placeCard(x, y, card)) {
            val pointsEarned = grid.calculatePoints(x, y)
            currentPlayer.addPoints(pointsEarned)
            notifyObservers(CardPlacementSuccess(x, y, card.toString, pointsEarned))
            grid.display() // Display updated grid
            true
          } else {
            notifyObservers(InvalidPlacement())
            false
          }
        case _ =>
          notifyObservers(InvalidPlacement())
          false
      }
    } match {
      case Success(result) => result
      case Failure(_) =>
        notifyObservers(InvalidPlacement())
        false
    }
  }

  def switchTurns(): Unit = {
    currentPlayer = if (currentPlayer == player1) player2 else player1
    notifyObservers(PlayerTurn(currentPlayer.name))
    grid.display() // Display updated grid after switching turns
  }

  def displayFinalScores(): Unit = {
    notifyObservers(GameOver(player1.name, player1.points, player2.name, player2.points))
  }
}