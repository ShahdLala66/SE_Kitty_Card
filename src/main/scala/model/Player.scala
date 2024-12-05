// src/main/scala/model/Player.scala
package model

import model.cards.Card
import model.*

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
        this.points = newPoints
    }

    def getHand: List[Card] = hand

    def updateHand(newHand: List[Card]): Unit = {
        hand = newHand
    }

    override def toString: String = s"$name (Points: $points)"
}