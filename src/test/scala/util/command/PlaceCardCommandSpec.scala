package util.command

import model.baseImp.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.*

class PlaceCardCommandSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A PlaceCardCommand" should {

        "execute correctly" in {
            val grid = mock[Grid]
            val player = Player("TestPlayer")
            val card = NumberCards(Suit.Green, Value.Four)
            val command = new PlaceCardCommand(grid, card, player, 0, (0, 0))

            when(grid.getCard(0, 0)).thenReturn(None)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            command.execute()
            verify(grid).placeCard(0, 0, card)
            player.points should be (0)
        }

        "undo correctly" in {
            val grid = mock[Grid]
            val player = Player("TestPlayer")
            val card = NumberCards(Suit.Green, Value.Four)
            val command = new PlaceCardCommand(grid, card, player, 0, (0, 0))

            when(grid.getCard(0, 0)).thenReturn(None)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            command.execute()
            val pointsAfterExecute = player.points
            command.undo()

            verify(grid).removeCard(0, 0)
            player.points should be (0)
        }

        "redo correctly" in {
            val grid = mock[Grid]
            val player = Player("TestPlayer")
            val card = NumberCards(Suit.Green, Value.Four)
            val command = new PlaceCardCommand(grid, card, player, 0, (0, 0))

            when(grid.getCard(0, 0)).thenReturn(None)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            command.execute()
            command.undo()
            command.redo()

            verify(grid, times(2)).placeCard(0, 0, card)
            player.points should be (0)
        }
    }
}