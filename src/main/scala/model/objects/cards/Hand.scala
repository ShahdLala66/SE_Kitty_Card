// src/main/scala/model/cards/Hand.scala
package model.objects.cards

class Hand {
  private var cards: List[Card] = List()

  def addCard(card: Card): Unit = {
    cards = card :: cards
  }

  def getCards: List[Card] = cards
  
  def updateHand(newCards: List[Card]): Unit = {
    cards = newCards
  }

  def removeCard(numberCards: NumberCards): Unit = {
    cards = cards.filterNot(_ == numberCards)
  }

  override def toString: String = cards.mkString(", ")
}