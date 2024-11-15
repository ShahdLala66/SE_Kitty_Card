// src/main/scala/model/Game.scala
package model

import cards.{NumberCards, Suit, Value}
import util.{GameEvent, Observable, PlayerTurn, CardDrawn, InvalidPlacement, CardPlacementSuccess, GameOver}
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

  private def gameLoop(): Unit = {
    while (deck.size > 0 && !grid.isFull) {
      notifyObservers(PlayerTurn(currentPlayer.name))
      currentPlayer.getHand.displayCards()
      handlePlayerTurn()
      switchTurns()
    }
  }

  private def handlePlayerTurn(): Unit = {
    currentPlayer.drawCard(deck) match {
      case Some(card) =>
        notifyObservers(CardDrawn(currentPlayer.name, card.toString))
        processPlayerInput()
      case None =>
        notifyObservers(InvalidPlacement())
    }
  }

  def processPlayerInput(): Unit = {
    var validInput = false
    while (!validInput) {
      val input = scala.io.StdIn.readLine()
      if (input.trim.toLowerCase == "draw") {
        currentPlayer.drawCard(deck)
        validInput = true
        notifyObservers(CardDrawn(currentPlayer.name, "another card"))
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

      currentPlayer.getHand.getCard(cardIndex) match {
        case Some(card: NumberCards) =>
          if (grid.placeCard(x, y, card)) {
            val pointsEarned = grid.calculatePoints(x, y)
            currentPlayer.addPoints(pointsEarned)
            notifyObservers(CardPlacementSuccess(x, y, card.toString, pointsEarned))
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
  }

  def displayFinalScores(): Unit = {
    notifyObservers(GameOver(player1.name, player1.points, player2.name, player2.points))
  }
}