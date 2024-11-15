package model.cards

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model.cards.{Hand, NumberCards, Suit, Value}

class HandSpec extends AnyWordSpec with Matchers {

    "A Hand" should {

        "add a card to the hand" in {
            val hand = new Hand
            val card = NumberCards(Suit.Green, Value.Seven)
            hand.addCard(card)
            hand.getCards should contain(card)
        }

        "display cards correctly" in {
            val hand = new Hand
            val card1 = NumberCards(Suit.Brown, Value.Seven)
            val card2 = NumberCards(Suit.Green, Value.One)
            hand.addCard(card1)
            hand.addCard(card2)
            // This is just to check if the method runs without error
            noException should be thrownBy hand.displayCards()
            
        }

        "get a card by index" in {
            val hand = new Hand
            val card = NumberCards(Suit.Red, Value.Seven)
            hand.addCard(card)
            hand.getCard(0) should be(Some(card))
            hand.getCard(1) should be(None)
        }

        "print 'Unknown card type' for unknown card types" in {
            val hand = new Hand
            val unknownCard = new Card {
                def getColor: String = "Unknown"
                override def toString: String = "Unknown card type"
            }
            hand.addCard(unknownCard)

            val output = new java.io.ByteArrayOutputStream()
            Console.withOut(output) {
                hand.displayCards()
            }

            output.toString should include("Unknown card type")
        }

        "convert hand to string" in {
            val hand = new Hand
            val card = NumberCards(Suit.Blue, Value.Seven)
            hand.addCard(card)
            hand.toString should be("Seven of Blue")
        }
    }
}
