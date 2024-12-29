// src/main/scala/model/Player.scala
package model.playerComp.baseImp

import model.cardComp.CardInterface
import model.deckComp.DeckInterface
import model.playerComp.PlayerInterface

case class Player(name: String, var points: Int = 0) extends PlayerInterface(name, points) {
    var hand: List[CardInterface] = List()


    def addPoints(newPoints: Int): Unit = {
        points += newPoints
    }

    def setPoints(newPoints: Int): Unit = {
        points = newPoints
    }

    def drawCard(deck: DeckInterface): Option[CardInterface] = {
        val drawnCard = deck.drawCard()
        drawnCard.foreach(card => hand = hand :+ card)
        drawnCard
    }

    def updatePoints(newPoints: Int): Unit = {
        points = newPoints
    }

    def getHand: List[CardInterface] = hand

    def updateHand(newHand: List[CardInterface]): Unit = {
        hand = newHand
    }

    def removeCard(card: CardInterface): Unit = {
        hand = hand.filterNot(_ == card)
    }
    
    def addCard(card: CardInterface): Unit = {
        hand = hand :+ card
    }

    override def toString: String = s"$name (Points: $points)"
}