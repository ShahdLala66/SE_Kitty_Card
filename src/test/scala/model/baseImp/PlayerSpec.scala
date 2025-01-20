package model.baseImp

import model.gameModelComp.baseImp.Player
import model.gameModelComp.{CardInterface, DeckInterface}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito.*
import org.scalatestplus.mockito.MockitoSugar

class PlayerSpec extends AnyWordSpec with Matchers with MockitoSugar {

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

        "set points correctly" in {
            val player = Player("TestPlayer")
            player.setPoints(10)
            player.points shouldBe 10
        }

        "draw a card from the deck" in {
            val deck = mock[DeckInterface]
            val card = mock[CardInterface]
            when(deck.drawCard()).thenReturn(Some(card))

            val player = Player("TestPlayer")
            player.drawCard(deck)
            player.getHand should contain(card)
        }

        "update points correctly" in {
            val player = Player("TestPlayer")
            player.updatePoints(20)
            player.points shouldBe 20
        }

        "update hand correctly" in {
            val player = Player("TestPlayer")
            val card1 = mock[CardInterface]
            val card2 = mock[CardInterface]
            val newHand = List(card1, card2)

            player.updateHand(newHand)
            player.getHand shouldBe newHand
        }

        "remove a card from the hand" in {
            val player = Player("TestPlayer")
            val card = mock[CardInterface]
            player.addCard(card)

            player.removeCard(card)
            player.getHand should not contain card
        }

        "add a card to the hand" in {
            val player = Player("TestPlayer")
            val card = mock[CardInterface]

            player.addCard(card)
            player.getHand should contain(card)
        }

        "update the player's hand correctly" in {
            val player = Player("TestPlayer")
            val card1 = mock[CardInterface]
            val card2 = mock[CardInterface]
            val newHand = List(card1, card2)

            player.setHand(newHand)
            player.getHand shouldBe newHand
        }

    }
}