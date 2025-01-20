package model.baseImp

import model.gameModelComp.CardInterface
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model.gameModelComp.baseImp.Hand

class HandSpec extends AnyWordSpec with Matchers {

    "A Hand" should {
        "start with no cards" in {
            val hand = new Hand()
            hand.getCards shouldBe empty
        }

        "allow adding a card" in {
            val hand = new Hand()
            val card = new CardInterface {
                override def getColor: String = "Red"
            }
            hand.addCard(card)
            hand.getCards should contain(card)
        }

        "return a string representation of the cards" in {
            val hand = new Hand()
            val card1 = new CardInterface {
                override def getColor: String = "Red"
                override def toString: String = "Card1"
            }
            val card2 = new CardInterface {
                override def getColor: String = "Blue"
                override def toString: String = "Card2"
            }
            hand.addCard(card1)
            hand.addCard(card2)
            hand.toString should include("Card1")
            hand.toString should include("Card2")
        }
    }
}