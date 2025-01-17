package util.command

import model.baseImp.*
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

class CommandManagerSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A CommandManager" should {

        "execute a command and update the state correctly" in {
            val grid = mock[Grid]
            val player = Player("TestPlayer")
            val card = NumberCards(Suit.Green, Value.Four)
            val command = new PlaceCardCommand(grid, card, player, 0, (0, 0))
            val commandManager = new CommandManager
            val initialState = new GameState(grid, List(player), 0, player.points)

            when(grid.getCard(0, 0)).thenReturn(None)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            val newState = commandManager.executeCommand(command, initialState)

            verify(grid).placeCard(0, 0, card)
            newState should be(initialState)
        }

        "undo a command and revert the state correctly" in {
            val grid = mock[Grid]
            val player = Player("TestPlayer")
            val card = NumberCards(Suit.Green, Value.Four)
            val command = new PlaceCardCommand(grid, card, player, 0, (0, 0))
            val commandManager = new CommandManager
            val initialState = new GameState(grid, List(player), 0, player.points)

            when(grid.getCard(0, 0)).thenReturn(None)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            commandManager.executeCommand(command, initialState)
            val undoState = commandManager.undo()

            verify(grid).removeCard(0, 0)
            undoState should not be null
        }

        "redo a command and reapply the state correctly" in {
            val grid = mock[Grid]
            val player = Player("TestPlayer")
            val card = NumberCards(Suit.Green, Value.Four)
            val command = new PlaceCardCommand(grid, card, player, 0, (0, 0))
            val commandManager = new CommandManager
            val initialState = new GameState(grid, List(player), 0, player.points)

            when(grid.getCard(0, 0)).thenReturn(None)
            when(grid.calculatePoints(0, 0)).thenReturn(10)

            commandManager.executeCommand(command, initialState)
            commandManager.undo()
            val redoState = commandManager.redo()

            verify(grid, times(2)).placeCard(0, 0, card)
            redoState should not be null
        }

        "return None when undo is called with an empty undo stack" in {
            val commandManager = new CommandManager
            val undoState = commandManager.undo()
            undoState should be(None)
        }

        "return None when redo is called with an empty redo stack" in {
            val commandManager = new CommandManager
            val redoState = commandManager.redo()
            redoState should be(None)
        }

    }
}