// src/main/scala/model/Player.scala
package model.objects

import model.objects.cards.{Card, NumberCards}

case class Player(name: String, var points: Int = 0) {
    var hand: List[Card] = List()

    def addPoints(newPoints: Int): Unit = {
        points += newPoints
    }

    def setPoints(newPoints: Int): Unit = {
        points = newPoints
    }

    def drawCard(deck: Deck): Option[Card] = {
        val drawnCard = deck.drawCard()
        drawnCard.foreach(card => hand = hand :+ card)
        drawnCard
    }

    def updatePoints(newPoints: Int): Unit = {
        points = newPoints
    }

    def getHand: List[Card] = hand

    def updateHand(newHand: List[Card]): Unit = {
        hand = newHand
    }

    def removeCard(card: Card): Unit = {
        hand = hand.filterNot(_ == card)
    }

    override def toString: String = s"$name (Points: $points)"
}