// src/main/scala/model/patterns/PlayerDecorator.scala
package model.patterns

import model.*
import model.cards.{Card, Hand}

abstract class PlayerDecorator(decoratedPlayer: Player) extends Player(decoratedPlayer.name) {
  var level: Int = 1
  var money: Int = 0

  override def drawCard(deck: Deck): Option[Card] = decoratedPlayer.drawCard(deck)

  override def addPoints(points: Int): Unit = {
    decoratedPlayer.addPoints(points)
    updateLevel()
  }

  override def getHand: Hand = decoratedPlayer.getHand

  private def updateLevel(): Unit = {
    level = decoratedPlayer.points / 100 // Example: 1 level for every 100 points
  }
}