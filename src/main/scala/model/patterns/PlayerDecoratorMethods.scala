// src/main/scala/model/LoggingPlayerDecorator.scala
package model.patterns

import model.cards.Card
import model.{Deck, Player}

class PlayerDecoratorMethods(decoratedPlayer: Player) extends PlayerDecorator(decoratedPlayer) {
  override def drawCard(deck: Deck): Option[Card] = {
    val card = super.drawCard(deck)
    println(s"${decoratedPlayer.name} drew a card: $card")
    card
  }

  override def addPoints(points: Int): Unit = {
    super.addPoints(points)
    println(s"${decoratedPlayer.name} earned $points points. Total points: ${decoratedPlayer.points}")
  }
}