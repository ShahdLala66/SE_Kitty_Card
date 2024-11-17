package model

import model.cards.{Hand, NumberCards}
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class GameSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A Game" should {

        "start with initial card distribution and display initial colors" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            game.start()

            verify(player1, times(3)).drawCard(deck)
            verify(player2, times(3)).drawCard(deck)
            verify(grid).displayInitialColors()
        }

        "switch turns correctly" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            game.switchTurns()
            game.currentPlayer should be(player2)

            game.switchTurns()
            game.currentPlayer should be(player1)
        }

        "handle card placement correctly" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            val hand = mock[Hand]
            val card = mock[NumberCards]
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List(card))
            when(grid.placeCard(0, 0, card)).thenReturn(true)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            val input = "0 0 0"
            val result = game.handleCardPlacement(input)

            result should be(true)
            verify(player1).addPoints(10)
            verify(grid).display()
        }
    }
}
