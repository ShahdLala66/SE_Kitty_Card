package model.cards

import model.objects.cards.{Card, Hand}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

class HandSpec extends AnyWordSpec {

    "A Hand" should {
        "return the correct cards" in {
            val hand = new Hand
            val card = new Card {
                override def getColor: String = "Brown"
                override def toString: String = "Card"
            }
            hand.addCard(card)
            hand.getCards shouldBe List(card)
        }

        "return the correct string representation" in {
            val hand = new Hand
            val card = new Card {
                override def getColor: String = "Red"
                override def toString: String = "Card"
            }
            hand.addCard(card)
            hand.toString shouldBe "Card"
        }
    }
}