package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {

    "A Player" should {

        "be created with a name and default points of 0" in {
            val player = Player("Alice")
            player.points shouldBe 0
        }

        "allow points to be added with addPoints" in {
            val player = Player("Bob")
            player.addPoints(10)
            player.points shouldBe 10
        }

        "update points correctly when addPoints is called multiple times" in {
            val player = Player("Charlie", 5)
            player.addPoints(3)
            player.addPoints(2)
            player.points shouldBe 10
        }

        "have a readable string representation" in {
            val player = Player("Dana", 20)
            player.toString shouldBe "Dana (Points: 20)"
        }

        "return the player's hand" in {
            val player = Player("Frank")
            val hand = player.getHand
            hand should not be null

        }

        "draw a card from the deck and add it to the player's hand" in {
            val player = Player("Eve")
            val deck = new Deck
            val card = player.drawCard(deck)
            card should not be None
            player.getHand.getCards.length shouldBe 1
        }
    }
}
