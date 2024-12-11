// src/main/scala/model/objects/cards/AssistCardHandler.scala
package model.objects.cards

import model.objects.{Grid, Player}

object AssistCardHandler {

  def handleAssistCard(cardName: String, currentPlayer: Player, opponentPlayer: Player, grid: Grid): Unit = {
    cardName.toLowerCase match {
      case "skip" => skipEffect()
      case "meowster" => meowsterEffect(currentPlayer, opponentPlayer)
      case "freeze" => freezeEffect()
      case "purrator" => purratorEffect(grid)
      case "meow this!" => meowThisEffect()
      case "kitty pow!" => kittyPowEffect(currentPlayer, opponentPlayer)
      case "cat-ching!" => catChingEffect(currentPlayer)
      case "magic paw" => magicPawEffect(grid)
      case "kitty plot" => kittyPlotEffect(currentPlayer)
      case "bye-bye!" => byeByeEffect(opponentPlayer, grid)
      case "purrceive" => purrceiveEffect(opponentPlayer)
      case "paw combo" => pawComboEffect(currentPlayer)
      case _ => println(s"Unknown card effect for card: $cardName")
    }
  }

  private def skipEffect(): Unit = {
    println("Skip effect: Opponent can’t play number cards this turn.")
  }

  private def meowsterEffect(currentPlayer: Player, opponentPlayer: Player): Unit = {
    println("Meowster effect: Steal a card from the opponent!")
    if (opponentPlayer.getHand.nonEmpty) {
      val stolenCard = opponentPlayer.getHand.head
      opponentPlayer.removeCard(stolenCard)
      currentPlayer.addCard(stolenCard)
      println(s"${currentPlayer.name} stole $stolenCard from ${opponentPlayer.name}")
    } else {
      println(s"${opponentPlayer.name} has no cards to steal!")
    }
  }

  private def freezeEffect(): Unit = {
    println("Freeze effect: Opponent loses their next turn completely!")
  }

  private def purratorEffect(grid: Grid): Unit = {
    println("Purrator effect: Randomly change the color of a grid cell.")
    val randomCell = grid.getRandomCell()
    if (randomCell != null) {
      randomCell.color = Grid.getRandomColor()
      println(s"Cell at ${randomCell.coordinates} changes to a new random color!")
    }
  }

  private def meowThisEffect(): Unit = {
    println("Meow This! effect: Cancel the enemy's Assist Card effect.")
  }

  private def kittyPowEffect(currentPlayer: Player, opponentPlayer: Player): Unit = {
    println("Kitty Pow! effect: Both players lose all their number cards.")
    currentPlayer.clearNumberCards()
    opponentPlayer.clearNumberCards()
    println("Both players lost all their number cards!")
  }

  private def catChingEffect(currentPlayer: Player): Unit = {
    println("Cat-ching! effect: Gain two number cards.")
    currentPlayer.drawCard(deck)
    currentPlayer.drawCard(deck)
  }

  private def magicPawEffect(grid: Grid): Unit = {
    println("Magic Paw effect: Enemy grid points reduce to 1 (except if they are doubled).")
    grid.reduceEnemyPointsToOne()
  }

  private def kittyPlotEffect(currentPlayer: Player): Unit = {
    println("Kitty Plot effect: Gain two Assist Cards, then discard one card from your hand (Number or Assist Card).")
    currentPlayer.drawAssistCard(deck)
    currentPlayer.drawAssistCard(deck)
    println(s"Select a card to discard from your hand: ${currentPlayer.getHand}")
  }

  private def byeByeEffect(opponentPlayer: Player, grid: Grid): Unit = {
    println("Bye-bye! effect: Remove an opponent's number card from the grid.")
    val targetCard = grid.getLastCardPlacedByPlayer(opponentPlayer)
    if (targetCard != null) {
      grid.removeCard(targetCard) 
      println(s"Removed ${targetCard} from the grid.")
    } else {
      println(s"No cards found on the grid placed by ${opponentPlayer.name}")
    }
  }

  private def purrceiveEffect(opponentPlayer: Player): Unit = {
    println("Purrceive effect: Look at 3 cards from the opponent and destroy one.")
    val threeCards = opponentPlayer.getHand.take(3)
    println(s"Opponent cards to choose from: $threeCards")
    if (threeCards.nonEmpty) {
      val cardToDestroy = threeCards.head
      opponentPlayer.removeCard(cardToDestroy)
      println(s"Destroyed $cardToDestroy from ${opponentPlayer.name}'s hand.")
    }
  }

  private def pawComboEffect(currentPlayer: Player): Unit = {
    println("Paw Combo effect: Play two number cards at once!")
  }
}