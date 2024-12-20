package model.cards

import model.objects.cards.Card
import org.scalatest.wordspec.AnyWordSpec

class CardsSpec extends AnyWordSpec {

    "A Card" should {
        "return the correct color" in {
            val card = new Card {
                override def getColor: String = "Red"
                override def toString: String = "Card"
            }
            assert(card.getColor == "Red")
        }

        "return the correct string representation" in {
            val card = new Card {
                override def getColor: String = "Red"
                override def toString: String = "Card"
            }
            assert(card.toString == "Card")
        }
    }
}