package model

import model.gameModelComp.{CardInterface, DeckInterface, HandInterface, PlayerInterface}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

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

    "A PlayerInterface" should {

        "be created with a name and default points of 0" in {
            val player: PlayerInterface = new PlayerInterface("Alice") {
                var hand: List[CardInterface] = List()

                override def addPoints(newPoints: Int): Unit = pointsI += newPoints

                override def setPoints(newPoints: Int): Unit = pointsI = newPoints

                override def drawCard(deck: DeckInterface): Option[CardInterface] = None

                override def updatePoints(newPoints: Int): Unit = pointsI = newPoints

                override def getHand: List[CardInterface] = hand

                override def updateHand(newHand: List[CardInterface]): Unit = hand = newHand

                override def removeCard(card: CardInterface): Unit = hand = hand.filterNot(_ == card)

                override def setHand(newHand: List[CardInterface]): Unit = hand = newHand
            }
            player.getPoints shouldBe 0
        }

        "allow points to be added with addPoints" in {
            val player: PlayerInterface = new PlayerInterface("Bob") {
                var hand: List[CardInterface] = List()

                override def addPoints(newPoints: Int): Unit = pointsI += newPoints

                override def setPoints(newPoints: Int): Unit = pointsI = newPoints

                override def drawCard(deck: DeckInterface): Option[CardInterface] = None

                override def updatePoints(newPoints: Int): Unit = pointsI = newPoints

                override def getHand: List[CardInterface] = hand

                override def updateHand(newHand: List[CardInterface]): Unit = hand = newHand

                override def removeCard(card: CardInterface): Unit = hand = hand.filterNot(_ == card)

                override def setHand(newHand: List[CardInterface]): Unit = hand = newHand
            }
            player.addPoints(10)
            player.getPoints shouldBe 10
        }

    }

}