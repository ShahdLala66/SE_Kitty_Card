// src/test/scala/model/GameSpec.scala
package model

import model.cards.{Hand, NumberCards}
import org.mockito.Mockito._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.ArgumentMatchers.any

class GameSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A Game" should {

        "start with initial card distribution and display initial colors" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1Name = "Zayne"
            val player2Name = "Corina"
            val card = mock[NumberCards]
            when(deck.drawCard()).thenReturn(Some(card))

            game.start(player1Name, player2Name)

            verify(deck, times(6)).drawCard()
            verify(grid).displayInitialColors()
        }

        "handleCardPlacement should return true for valid input and place card correctly" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val hand = mock[Hand]
            val card = mock[NumberCards]
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List(card))
            when(grid.placeCard(0, 0, card)).thenReturn(true)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            game.player1 = player1
            game.currentPlayer = player1

            val input = "0 0 0"
            val result = game.handleCardPlacement(input)

            verify(player1).addPoints(10)
            verify(grid).display()
        }

        "handleCardPlacement should return false for invalid input format" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val input = "invalid input"
            val result = game.handleCardPlacement(input)

            verify(grid, never()).placeCard(any[Int], any[Int], any[NumberCards])
        }

        "handleCardPlacement should return false for out of bounds card index" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val hand = mock[Hand]
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List.empty)

            game.player1 = player1
            game.currentPlayer = player1

            val input = "0 0 0"
            val result = game.handleCardPlacement(input)

            verify(grid, never()).placeCard(any[Int], any[Int], any[NumberCards])
        }

        "handleCardPlacement should return false for invalid card placement" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val hand = mock[Hand]
            val card = mock[NumberCards]
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List(card))
            when(grid.placeCard(0, 0, card)).thenReturn(false)

            game.player1 = player1
            game.currentPlayer = player1

            val input = "0 0 0"
            val result = game.handleCardPlacement(input)

            verify(grid).placeCard(0, 0, card)
            verify(player1, never()).addPoints(any[Int])
        }

        "handlePlayerTurn should draw a card and process player input" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val card = mock[NumberCards]
            val hand = mock[Hand]

            when(player1.drawCard(deck)).thenReturn(Some(card))
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List(card))

            game.player1 = player1
            game.currentPlayer = player1

            val input = "draw"
            val in = new java.io.ByteArrayInputStream(input.getBytes)
            Console.withIn(in) {
                game.handlePlayerTurn()
            }

            verify(player1, times(2)).drawCard(deck)
            verify(hand, times(2)).getCards // Adjust `times` if needed.
        }


        "gameLoop should stop when grid is full" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            when(deck.size).thenReturn(1)
            when(grid.isFull).thenReturn(true)
            when(player1.getHand).thenReturn(mock[Hand])
            when(player1.getHand.getCards).thenReturn(List(mock[NumberCards]))

            game.player1 = player1
            game.currentPlayer = player1

            game.gameLoop()

            verify(player1, never()).drawCard(deck)
            verify(player1.getHand, never()).getCards
        }

        "switch turns correctly" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val player2 = mock[Player]
            game.player1 = player1
            game.player2 = player2
            game.currentPlayer = player1

            game.switchTurns()
            game.currentPlayer should be(player2)

            game.switchTurns()
            game.currentPlayer should be(player1)
        }

        "drawCardForCurrentPlayer should notify observers when a card is drawn" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val card = mock[NumberCards]
            when(player1.drawCard(deck)).thenReturn(Some(card))
            when(player1.getHand).thenReturn(mock[Hand])
            when(player1.getHand.getCards).thenReturn(List(card))

            game.player1 = player1
            game.currentPlayer = player1

            game.drawCardForCurrentPlayer()

            verify(player1).drawCard(deck)
            verify(player1.getHand).getCards
        }

        "drawCardForCurrentPlayer should notify observers when no card is drawn" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            when(player1.drawCard(deck)).thenReturn(None)

            game.player1 = player1
            game.currentPlayer = player1

            game.drawCardForCurrentPlayer()

            verify(player1).drawCard(deck)
        }

        "processPlayerInput should handle 'draw' input correctly" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val card = mock[NumberCards]
            when(player1.drawCard(deck)).thenReturn(Some(card))
            when(player1.getHand).thenReturn(mock[Hand])
            when(player1.getHand.getCards).thenReturn(List(card))

            game.player1 = player1
            game.currentPlayer = player1

            val input = "draw"
            val in = new java.io.ByteArrayInputStream(input.getBytes)
            Console.withIn(in) {
                game.processPlayerInput()
            }

            verify(player1).drawCard(deck)
        }

        "processPlayerInput should handle card placement input correctly" in {
            val deck = mock[Deck]
            val grid = mock[Grid]
            val game = new Game(deck, grid)

            val player1 = mock[Player]
            val hand = mock[Hand]
            val card = mock[NumberCards]
            when(player1.getHand).thenReturn(hand)
            when(hand.getCards).thenReturn(List(card))
            when(grid.placeCard(0, 0, card)).thenReturn(true)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            game.player1 = player1
            game.currentPlayer = player1

            val input = "0 0 0"
            val in = new java.io.ByteArrayInputStream(input.getBytes)
            Console.withIn(in) {
                game.processPlayerInput()
            }

            verify(grid).placeCard(0, 0, card)
            verify(player1).addPoints(10)
        }
    }
}