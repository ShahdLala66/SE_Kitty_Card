package aview

import controller.GameControllerInterface
import model.{CardInterface, PlayerInterface}
import model.baseImp.{Player, Suit}
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import util.*

import java.io.ByteArrayOutputStream


class TuiSpec extends AnyWordSpec with MockitoSugar {
    "A Tui" should {
        val gameControllerMock = mock[GameControllerInterface]
        val inputProviderMock = mock[InputProvider]
        val tui = new Tui(gameControllerMock)
        tui.inputProvider = inputProviderMock

        "handle AskForLoadGame event correctly when loading a saved game" in {
            when(inputProviderMock.getInput).thenReturn("2")
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(AskForLoadGame)
            }

            verify(gameControllerMock).handleCommand("load")
            val expectedOutput = "Would you like to:\n(1) Start new game\n(2) Load saved game"
            outStream.toString should not be empty
        }

        "handle GameSaved event correctly" in {
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(GameSaved)
            }
            val expectedOutput = "Game saved successfully"
            outStream.toString should include(expectedOutput)
        }

        "handle UpdateLoadedGame event correctly" in {
            val card1 = mock[CardInterface]
            val card2 = mock[CardInterface]
            when(card1.toString).thenReturn("Card1")
            when(card2.toString).thenReturn("Card2")
            val gridColors = List(
                (0, 0, Some(card1), Suit.Red),
                (0, 1, Some(card2), Suit.Green)
            )
            val currentPlayer = mock[Player]
            val player1 = mock[Player]
            val player2 = mock[Player]
            val hand = List(mock[CardInterface], mock[CardInterface])

            when(currentPlayer.getPlayerName).thenReturn("CurrentPlayer")
            when(player1.getPlayerName).thenReturn("Player1")
            when(player2.getPlayerName).thenReturn("Player2")
            when(hand.head.toString).thenReturn("Card1")
            when(hand(1).toString).thenReturn("Card2")
            when(gameControllerMock.getGridColors).thenReturn(gridColors)

            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(UpdateLoadedGame(gridColors, currentPlayer, player1, player2, hand))
            }

            val expectedOutput = "Game loaded successfully\n" +
                "Rectangle at (0, 0) has card: Card1 and color: Red\n" +
                "Rectangle at (0, 1) has card: Card2 and color: Green\n\n" +
                "Current players: Player1 vs Player2\n" +
                "CurrentPlayer's turn!\n\n" +
                "Your cards:\nCard1\nCard2\n"

            val actualOutput = outStream.toString.replace("\r\n", "\n")
            actualOutput should not be empty
        }

        "process PlayerTurn event correctly" in {
            reset(inputProviderMock)
            when(inputProviderMock.getInput).thenReturn("some input")
            tui.update(PlayerTurn("Player1"))
            verify(inputProviderMock).getInput
            val tuiSpy = spy(new Tui(gameControllerMock))
            tuiSpy.inputProvider = inputProviderMock
            tuiSpy.update(PlayerTurn("Player1"))
            verify(tuiSpy).processInput("some input")
        }

        "handle CardDrawn event correctly" in {
            val playerName = "Player1"
            val card = "One of Brown"
            val expectedOutput = s"\n$playerName drew: $card\n"
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(CardDrawn(playerName, card))
            }
            outStream.toString.contains(expectedOutput)
        }

        "handle UpdatePlayer event correctly" in {
            val player1 = "Player1"
            val expectedOutput = s"\nIt's $player1's turn!\n"
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(UpdatePlayer(player1))
            }
            outStream.toString.contains(expectedOutput)
        }


        "handle InvalidPlacement event correctly" in {
            val outStream = new java.io.ByteArrayOutputStream()
            val currentPlayerMock = mock[PlayerInterface]
            when(gameControllerMock.getCurrentplayer).thenReturn(currentPlayerMock)
            when(currentPlayerMock.getPlayerName).thenReturn("Player1")
            when(gameControllerMock.getCurrentplayer.getHand).thenReturn(List.empty)
            when(gameControllerMock.getGridColors).thenReturn(List.empty) // Mock getGridColors to return an empty list

            Console.withOut(outStream) {
                tui.update(InvalidPlacement)
            }

            val expectedOutputPart1 = "Invalid placement. Spot is either occupied or out of bounds. Turn forfeited."
            val expectedOutputPart2 = s"It's ${gameControllerMock.getCurrentplayer.getPlayerName}'s turn!"

            val actualOutput = outStream.toString
            println(actualOutput.contains(expectedOutputPart1))
            println(actualOutput.contains(expectedOutputPart2))
        }

        "handle CardPlacementSuccess event correctly" in {
            val x = 1
            val y = 2
            val card = "One of Brown"
            val points = 10
            val expectedOutput = s"\nCard placed at ($x, $y): $card. You have earned $points points!"
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(CardPlacementSuccess(x, y, card, points))
            }
            outStream.toString.contains(expectedOutput)
        }

        "handle GameOver event correctly" in {
            val player1Name = "Alice"
            val player1Points = 10
            val player2Name = "Bob"
            val player2Points = 8
            val expectedOutputPart1 = Console.RED + "\n\nGame over!"
            val expectedOutputPart2 = Console.YELLOW + s"$player1Name's final score: $player1Points"
            val expectedOutputPart3 = s"$player2Name's final score: $player2Points"
            val expectedOutputPart4 = s"$player1Name wins!" + Console.RESET

            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(GameOver(player1Name, player1Points, player2Name, player2Points))
            }

            val actualOutput = outStream.toString
            actualOutput.contains(expectedOutputPart4)
        }

        "handle GameOver event correctly when player2 wins" in {
            val player1Name = "Alice"
            val player1Points = 8
            val player2Name = "Bob"
            val player2Points = 10
            val expectedOutputPart1 = Console.RED + "\n\nGame over!"
            val expectedOutputPart2 = Console.YELLOW + s"$player1Name's final score: $player1Points"
            val expectedOutputPart3 = s"$player2Name's final score: $player2Points"
            val expectedOutputPart4 = s"$player2Name wins!" + Console.RESET

            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(GameOver(player1Name, player1Points, player2Name, player2Points))
            }

            val actualOutput = outStream.toString
            actualOutput.contains(expectedOutputPart4)
        }

        "handle GameOver event correctly when it's a tie" in {
            val player1Name = "Alice"
            val player1Points = 10
            val player2Name = "Bob"
            val player2Points = 10
            val expectedOutputPart1 = Console.RED + "\n\nGame over!"
            val expectedOutputPart2 = Console.YELLOW + s"$player1Name's final score: $player1Points"
            val expectedOutputPart3 = s"$player2Name's final score: $player2Points"
            val expectedOutputPart4 = "It's a tie!" + Console.RESET

            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(GameOver(player1Name, player1Points, player2Name, player2Points))
            }

            val actualOutput = outStream.toString
            actualOutput.contains(expectedOutputPart4)
        }

        "handle UpdateGrid event correctly" in {
            val size = 2
            val rectangleColors = Array(
                Array(Suit.Red, Suit.Green),
                Array(Suit.Green, Suit.Red)
            )
            val grid = model.baseImp.Grid(size, rectangleColors)
            when(gameControllerMock.getGridColors).thenReturn(grid.toArray.flatten.zipWithIndex.map {
                case ((card, color), index) =>
                    val x = index / size
                    val y = index % size
                    (x, y, card.map(_.toString), color)
            }.toList)

            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(UpdateGrid(grid))
            }

            val expectedOutput = "Rectangle at (0, 0) has card: Empty and color: Red\n" +
                "Rectangle at (0, 1) has card: Empty and color: Green\n" +
                "Rectangle at (1, 0) has card: Empty and color: Green\n" +
                "Rectangle at (1, 1) has card: Empty and color: Red\n\n"

            val actualOutput = outStream.toString.replaceAll("\\s", "")
            val expectedOutputCleaned = expectedOutput.replaceAll("\\s", "")
            if (actualOutput != expectedOutputCleaned) {
                throw new AssertionError(s"Expected: $expectedOutputCleaned but got: $actualOutput")
            }
        }

        "handle UndoEvent correctly" in {
            val outStream = new java.io.ByteArrayOutputStream()
            val gameStateMock = mock[util.command.GameState]
            Console.withOut(outStream) {
                tui.update(UndoEvent(gameStateMock))
            }
            val expectedOutput = Console.RED + "\nUndo performed." + Console.RESET
            outStream.toString should include(expectedOutput)
        }

        "handle RedoEvent correctly" in {
            val outStream = new java.io.ByteArrayOutputStream()
            val gameStateMock = mock[util.command.GameState]
            Console.withOut(outStream) {
                tui.update(RedoEvent(gameStateMock))
            }
            val expectedOutput = Console.RED + "\nRedo performed." + Console.RESET
            outStream.toString should include(expectedOutput)
        }

        "handle ShowCardsForPlayer event correctly" in {
            val card1 = mock[model.CardInterface]
            val card2 = mock[model.CardInterface]
            val card3 = mock[model.CardInterface]
            when(card1.toString).thenReturn("Card1")
            when(card2.toString).thenReturn("Card2")
            when(card3.toString).thenReturn("Card3")

            val cards = List(card1, card2, card3)
            val expectedOutput = cards.map(_.toString).mkString("\n")
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(ShowCardsForPlayer(cards))
            }
            val actualOutput = outStream.toString.replace("\r\n", "\n")
            actualOutput should include(expectedOutput)
        }

        "handle PromptForPlayerName event correctly" in {
            val player1Name = "Alice"
            val player2Name = "Bob"
            when(inputProviderMock.getInput).thenReturn(player1Name, player2Name)

            tui.update(PromptForPlayerName)

            verify(inputProviderMock, times(4)).getInput
            verify(gameControllerMock).promptForPlayerName(player1Name, player2Name)
        }

        "handle UpdatePlayers event correctly" in {
            val player1 = mock[Player]
            val player2 = mock[Player]
            when(player1.name).thenReturn("Player1")
            when(player2.name).thenReturn("Player2")

            val expectedOutput = "The Players are Player1 and Player2\nLet's start the game!"
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(UpdatePlayers(player1, player2))
            }

            val actualOutput = outStream.toString.replace("\r\n", "\n")
            actualOutput should include(expectedOutput)
        }

        "handle AskForGameMode event correctly" in {
            val outStream = new java.io.ByteArrayOutputStream()
            reset(inputProviderMock)
            when(inputProviderMock.getInput).thenReturn("s")

            Console.withOut(outStream) {
                tui.update(AskForGameMode)
            }

            verify(inputProviderMock, times(1)).getInput
            verify(gameControllerMock).setGameMode("s")
            val expectedOutput = "Single- or Multiplayer (s/m):"
            outStream.toString should include(expectedOutput)
        }

        "handle unknown event correctly" in {
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(UnknownEvent)
            }
            val expectedOutput = "Invalid event."
            outStream.toString should include(expectedOutput)
        }

        "print colored cat" in {
            val gameControllerMock = mock[GameControllerInterface]
            val tui = new Tui(gameControllerMock)
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printColoredCat("\u001b[32m")
            }
            outputStream.toString should include ("\u001b[32m ∧,,,∧")
        }

        "print the welcome message" in {
            val gameControllerMock = mock[GameControllerInterface]
            val tui = new Tui(gameControllerMock)
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.welcomeMessage()
            }
            outputStream.toString should include("Welcome to the Kitty Card Game!")
        }

        "print colored cats in a loop" in {
            val gameControllerMock = mock[GameControllerInterface]
            val tui = new Tui(gameControllerMock)
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printCatLoop()
            }
            val output = outputStream.toString
            tui.colors.values.foreach { color =>
                output should include(s"$color ∧,,,∧")
            }
        }

        "print bad choice" in {
            val gameControllerMock = mock[GameControllerInterface]
            val tui = new Tui(gameControllerMock)
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printBadChoice("Green")
            }
            outputStream.toString should include("\u001b[32m ∧,,,∧")
            outputStream.toString should include("\u001b[32m ( ̳- · -̳)")
            outputStream.toString should include("\u001b[32m/ づbad choiceづ")
        }

        "print meh" in {
            val gameControllerMock = mock[GameControllerInterface]
            val tui = new Tui(gameControllerMock)
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printMeh("Green")
            }
            outputStream.toString should include("\u001b[32m ∧,,,∧")
            outputStream.toString should include("\u001b[32m ( ̳- · -̳)")
            outputStream.toString should include("\u001b[32m/ づmehづ")
        }

        "handle undo command correctly" in {
            val inputProviderMock = mock[InputProvider]
            when(inputProviderMock.getInput).thenReturn("undo")
            tui.inputProvider = inputProviderMock

            tui.update(PlayerTurn("Player1"))

            verify(gameControllerMock).handleCommand("undo")
        }

        "handle redo command correctly" in {
            val inputProviderMock = mock[InputProvider]
            when(inputProviderMock.getInput).thenReturn("redo")
            tui.inputProvider = inputProviderMock

            tui.update(PlayerTurn("Player1"))

            verify(gameControllerMock).handleCommand("redo")
        }

        "handle draw command correctly" in {
            val inputProviderMock = mock[InputProvider]
            when(inputProviderMock.getInput).thenReturn("draw")
            tui.inputProvider = inputProviderMock

            tui.update(PlayerTurn("Player1"))

            verify(gameControllerMock).handleCommand("draw")
        }

        "handle card placement command correctly" in {
            val inputProviderMock = mock[InputProvider]
            when(inputProviderMock.getInput).thenReturn("1 2 3")
            tui.inputProvider = inputProviderMock

            tui.update(PlayerTurn("Player1"))

            verify(gameControllerMock).handleCardPlacement(1, 2, 3)
        }

        "handle invalid input correctly" in {
            val inputProviderMock = mock[InputProvider]
            when(inputProviderMock.getInput).thenReturn("invalid input")
            tui.inputProvider = inputProviderMock
            reset(gameControllerMock)
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(PlayerTurn("Player1"))
            }

            verify(gameControllerMock, times(2)).askForInputAgain()
            val expectedOutput = "Invalid input! Please use one of the following formats:"
            outStream.toString should include(expectedOutput)
        }

        "handle AskForLoadGame event correctly when starting a new game" in {
            when(inputProviderMock.getInput).thenReturn("1")

            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                tui.update(AskForLoadGame)
            }

            verify(gameControllerMock).handleCommand("start")
            val expectedOutput = "Would you like to:\n(1) Start new game\n(2) Load saved game"
            outStream.toString should not be empty
        }



    }
}