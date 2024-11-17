package model

import model.cards.{Hand, NumberCards}
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.ArgumentMatchers.any

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

        "handleCardPlacement should return true for valid input and place card correctly" in {
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

        "handleCardPlacement should return false for invalid input format" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            val input = "invalid input"
            val result = game.handleCardPlacement(input)

            result should be(false)
            verify(grid, never()).placeCard(any[Int], any[Int], any[NumberCards])
        }

        "handleCardPlacement should return false for out of bounds card index" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            val hand = mock[Hand]
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List.empty)

            val input = "0 0 0"
            val result = game.handleCardPlacement(input)

            result should be(false)
            verify(grid, never()).placeCard(any[Int], any[Int], any[NumberCards])
        }

        "handleCardPlacement should return false for invalid card placement" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            val hand = mock[Hand]
            val card = mock[NumberCards]
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List(card))
            when(grid.placeCard(0, 0, card)).thenReturn(false)

            val input = "0 0 0"
            val result = game.handleCardPlacement(input)

            result should be(false)
            verify(grid).placeCard(0, 0, card)
            verify(player1, never()).addPoints(any[Int])
        }



        "gameLoop should stop when grid is full" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            when(deck.size).thenReturn(1)
            when(grid.isFull).thenReturn(true)
            when(player1.getHand).thenReturn(mock[Hand])
            when(player1.getHand.getCards).thenReturn(List(mock[NumberCards]))

            game.gameLoop()

            verify(player1, never()).drawCard(deck)
            verify(player1.getHand, never()).getCards
            verify(player2, never()).drawCard(deck)
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


        "drawCardForCurrentPlayer should notify observers when a card is drawn" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            val card = mock[NumberCards]
            when(player1.drawCard(deck)).thenReturn(Some(card))
            when(player1.getHand).thenReturn(mock[Hand])
            when(player1.getHand.getCards).thenReturn(List(card))

            game.drawCardForCurrentPlayer()

            verify(player1).drawCard(deck)
            verify(player1.getHand).getCards
        }

        "drawCardForCurrentPlayer should notify observers when no card is drawn" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            when(player1.drawCard(deck)).thenReturn(None)

            game.drawCardForCurrentPlayer()

            verify(player1).drawCard(deck)
        }

        "processPlayerInput should handle 'draw' input correctly" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(player1, player2, deck, grid)

            val card = mock[NumberCards]
            when(player1.drawCard(deck)).thenReturn(Some(card))
            when(player1.getHand).thenReturn(mock[Hand])
            when(player1.getHand.getCards).thenReturn(List(card))

            val input = "draw"
            val in = new java.io.ByteArrayInputStream(input.getBytes)
            Console.withIn(in) {
                game.processPlayerInput()
            }

            verify(player1).drawCard(deck)
        }


    }
}
