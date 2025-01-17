package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ModelInterfaceSpec extends AnyWordSpec with Matchers {

    "A CardInterface" should {
        "return its color" in {
            val card = new CardInterface {
                override def getColor: String = "Red"
                override def toString: String = "Red Card"
            }
            card.getColor should be ("Red")
            card.toString should be ("Red Card")
        }
    }

    "A DeckInterface" should {
        "draw a card and return the correct size" in {
            val card = new CardInterface {
                override def getColor: String = "Red"
                override def toString: String = "Red Card"
            }
            val deck = new DeckInterface {
                private var cards = List(card)
                override def drawCard(): Option[CardInterface] = {
                    val drawnCard = cards.headOption
                    cards = cards.drop(1)
                    drawnCard
                }
                override def size: Int = cards.size
            }
            deck.size should be (1)
            deck.drawCard() should be (Some(card))
            deck.size should be (0)
        }
    }

    "A HandInterface" should {
        "add and return cards" in {
            val card = new CardInterface {
                override def getColor: String = "Red"
                override def toString: String = "Red Card"
            }
            val hand = new HandInterface {
                private var cards = List.empty[CardInterface]
                override def addCard(card: CardInterface): Unit = cards = card :: cards
                override def getCards: List[CardInterface] = cards
                override def toString: String = cards.mkString(", ")
            }
            hand.addCard(card)
            hand.getCards should contain (card)
            hand.toString should be ("Red Card")
        }
    }
}