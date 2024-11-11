package model

import model.cards.{Card, NumberCards, Hand}

case class Player(name: String, var points: Int = 0) {
    private var hand: Hand = new Hand()

    def addPoints(newPoints: Int): Unit = {
        points += newPoints
    }

    def drawCard(deck: Deck): Unit = {
        deck.drawCard().foreach(card => hand.addCard(card))
    }

    def getHand: Hand = hand

    override def toString: String = s"$name (Points: $points)"
}