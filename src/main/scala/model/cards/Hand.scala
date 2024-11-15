package model.cards

class Hand {
  private var cards: List[Card] = List()

  def addCard(card: Card): Unit = {
    cards = card :: cards
  }

  def getCards: List[Card] = cards

  def displayCards(): Unit = {
    println("Your hand:")
    cards.zipWithIndex.foreach {
      case (numberCard: NumberCards, index) => println(s"$index: ${numberCard.suit}, ${numberCard.value}")
      case (card, index) => println(s"$index: Unknown card type")
    }
  }

  def getCard(index: Int): Option[Card] = {
    if (index >= 0 && index < cards.length) Some(cards(index))
    else None
  }

  override def toString: String = cards.mkString(", ")
}