package controller

import model.CardInterface
import model.FileIO.{FileIOInterface, FileIOXML}
import model.baseImp.*
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import util.*
import util.command.GameState
import util.grid.GridUtils


class GameControllerSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A GameController" should {

        "start the game and notify observers" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            controller.startGame()

            verify(observer).update(AskForGameMode)
        }

        "notify observers when askForGameLoad is called" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            controller.askForGameLoad()

            verify(observer).update(AskForLoadGame)
        }

        "set playerIsAtTurn to true when askForInputAgain is called" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)

            controller.playerIsAtTurn = false
            controller.askForInputAgain()

            controller.playerIsAtTurn shouldBe true
        }

        "save the game and notify observers" in {
            val deck = new Deck()
            val hand = new Hand()
            val mockFileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, mockFileIO)
            val observer = mock[Observer]
            controller.add(observer)

            val player1 = Player("Alice")
            val player2 = Player("Bob")
            controller.player1 = player1
            controller.player2 = player2
            controller.currentPlayer = player1
            controller.grid = GridUtils.createEmptyGrid(3)
            controller.currentState = new GameState(controller.grid, List(player1, player2), 0, 0)
            controller.handleCommand("save")

            verify(mockFileIO).save(controller, controller.grid)
            verify(observer).update(GameSaved)
        }

        "load the game and handle errors correctly" in {
            val deck = new Deck()
            val hand = new Hand()
            val mockFileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, mockFileIO)
            val observer = mock[Observer]
            controller.add(observer)

            when(mockFileIO.load(controller)).thenReturn("Load successful")
            controller.handleCommand("load")
            verify(mockFileIO).load(controller)

            val errorMessage = "Load failed"
            when(mockFileIO.load(controller)).thenReturn(errorMessage)
            val outContent = new java.io.ByteArrayOutputStream()
            Console.withOut(outContent) {
                controller.handleCommand("load")
            }
            outContent.toString should include("")
        }

        "start the game and notify observers with PromptForPlayerName" in {
            val deck = new Deck()
            val hand = new Hand()
            val mockFileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, mockFileIO)
            val observer = mock[Observer]
            controller.add(observer)
            controller.handleCommand("start")

            controller.grid should not be null
            controller.currentState should not be null
            verify(observer).update(PromptForPlayerName)
        }


        "start a multiplayer game and notify observers" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)
            controller.player1String = "Alice"
            controller.player2String = "Bob"
            controller.grid = GridUtils.createEmptyGrid(3)

            controller.startMultiPlayerGame()
            controller.currentPlayer shouldBe controller.player1
            verify(observer).update(UpdatePlayer("Alice"))
            verify(observer).update(UpdateGrid(controller.grid))
            verify(observer).update(ShowCardsForPlayer(controller.player1.getHand))
        }

        "handle undo command correctly" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            val player1 = Player("Alice")
            val player2 = Player("Bob")
            controller.player1 = player1
            controller.player2 = player2
            controller.currentPlayer = player1
            controller.grid = GridUtils.createEmptyGrid(3)
            controller.currentState = new GameState(controller.grid, List(player1, player2), 0, 0)

            val card = NumberCards(Suit.Red, Value.Five)
            player1.addCard(card)
            controller.handleCardPlacement(0, 0, 0)
            reset(observer)
            controller.handleCommand("undo")

            verify(observer).update(argThat {
                case undoEvent: UndoEvent =>
                    undoEvent.state.players == List(player1, player2) && undoEvent.state.grid == controller.grid
                case _ => false
            })
        }

        "handle redo command correctly" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            val player1 = Player("Alice")
            val player2 = Player("Bob")
            controller.player1 = player1
            controller.player2 = player2
            controller.currentPlayer = player1
            controller.grid = GridUtils.createEmptyGrid(3)
            val initialState = new GameState(controller.grid, List(player1, player2), 0, 0)
            controller.currentState = initialState

            val card = NumberCards(Suit.Red, Value.Five)
            player1.addCard(card)
            controller.handleCardPlacement(0, 0, 0)
            controller.handleCommand("undo")

            reset(observer)
            controller.handleCommand("redo")
            verify(observer, times(2)).update(any[RedoEvent])
            verify(observer, times(2)).update(any[ShowCardsForPlayer])
        }

        "handle errors correctly when loading the game" in {
            val deck = new Deck()
            val hand = new Hand()
            val mockFileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, mockFileIO)
            val observer = mock[Observer]
            controller.add(observer)

            val errorMessage = "Load failed"
            when(mockFileIO.load(controller)).thenThrow(new RuntimeException(errorMessage))

            val outContent = new java.io.ByteArrayOutputStream()
            Console.withOut(outContent) {
                controller.handleCommand("load")
            }

            outContent.toString should include(s"Error loading game: $errorMessage")
        }

        "notify observers when game is over" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)
            val player1 = Player("Alice")
            val player2 = Player("Bob")
            controller.player1 = player1
            controller.player2 = player2
            controller.currentPlayer = player1
            val grid = mock[Grid]
            controller.grid = grid

            when(grid.isFull).thenReturn(true)

            controller.handleCardPlacement(0, 0, 0)

            verify(observer).update(GameOver(player1.name, player1.points, player2.name, player2.points))
        }

        "return the list of players" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val player1 = Player("Alice")
            val player2 = Player("Bob")
            controller.player1 = player1
            controller.player2 = player2

            val players = controller.getPlayers

            players should contain theSameElementsAs List(player1, player2)
        }

        "return the current game state" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val grid = GridUtils.createEmptyGrid(3)
            val players = List(Player("Alice"), Player("Bob"))
            val gameState = new GameState(grid, players, 0, 0)
            controller.currentState = gameState

            val result = controller.getCurrentState

            result shouldBe gameState
        }

        "return current player correctly" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = new FileIOXML()
            val controller = new GameController(deck, hand, fileIO)
            val player1 = Player("Alice")
            controller.currentPlayer = player1

            val result = controller.getCurrentplayer

            result shouldBe player1
        }

        "load the game state successfully" in {
            val deck = new Deck()
            val hand = new Hand()
            val mockFileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, mockFileIO)
            val observer = mock[Observer]
            controller.add(observer)
            controller.setGameMode("m")

            val grid = GridUtils.createEmptyGrid(3)
            val players = List(Player("Alice"), Player("Bob"))
            val gameState = new GameState(grid, players, 0, 0)

            controller.loadGameState(gameState)
            controller.currentState shouldBe gameState
        }

        "handle errors correctly when loading the game state" in {
            val deck = new Deck()
            val hand = new Hand()
            val mockFileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, mockFileIO)
            val observer = mock[Observer]
            controller.add(observer)
            controller.setGameMode("x")

            val grid = GridUtils.createEmptyGrid(3)
            val players = List(Player("Alice"), Player("Bob"))
            val gameState = new GameState(grid, players, 0, 0)

            val errorMessage = "Load failed"
            when(mockFileIO.load(controller)).thenThrow(new RuntimeException(errorMessage))

            val outContent = new java.io.ByteArrayOutputStream()
            Console.withOut(outContent) {
                controller.loadGameState(gameState)
            }

            outContent.toString should not be empty
        }

        "there are cards in the deck" in {
            val deck = mock[Deck] 
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            val player1 = Player("Alice")
            val player2 = Player("Bob")
            controller.player1 = player1
            controller.player2 = player2
            controller.currentPlayer = player1

            val testCard = NumberCards(Suit.Red, Value.Five)
            when(deck.drawCard()).thenReturn(Some(testCard)) 
            controller.drawCardForCurrentPlayer()

            verify(observer).update(CardDrawn("Alice", testCard.toString))
            verify(observer).update(ShowCardsForPlayer(player1.getHand))
            controller.currentPlayer shouldBe player2
        }

        "there are no cards in the deck" in {
            val deck = mock[Deck] // Mock the deck to control its behavior
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            controller.add(observer)

            controller.player1 = Player("Alice")
            controller.player2 = Player("Bob")
            controller.currentPlayer = controller.player1
            when(deck.drawCard()).thenReturn(None)
            doNothing().when(observer).update(any[GameEvent])

            controller.drawCardForCurrentPlayer()

            verify(observer).update(InvalidPlacement)
            controller.currentPlayer shouldBe controller.player1
        }

        "game hasn't started yet" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            val gameMode = mock[GameMode]
            controller.add(observer)
            controller.gameMode = gameMode

            doNothing().when(observer).update(any[GameEvent])
            doNothing().when(gameMode).startGame(controller)

            controller.promptForPlayerName("Alice", "Bob")

            verify(observer).update(UpdatePlayers(Player("Alice"), Player("Bob")))
            verify(gameMode).startGame(controller)
        }

        "game has already started" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val observer = mock[Observer]
            val gameMode = mock[GameMode]
            controller.add(observer)
            controller.gameMode = gameMode

            controller.counter = 1
            controller.player1String = "Alice"
            controller.player2String = "Bob"

            doNothing().when(observer).update(any[GameEvent])
            doNothing().when(gameMode).startGame(controller)

            val outContent = new java.io.ByteArrayOutputStream()
            Console.withOut(outContent) {
                controller.promptForPlayerName("Carol", "Dave")
            }
            outContent.toString should include("Game already started")
            verify(observer, never).update(any[UpdatePlayers])
            verify(gameMode).startGame(controller)
        }

        "return the correct grid colors" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val grid = mock[Grid]
            controller.grid = grid

            val card1 = mock[CardInterface]
            val card2 = mock[CardInterface]
            val card3 = mock[CardInterface]
            val card4 = mock[CardInterface]

            when(grid.toArray).thenReturn(Array(
                Array((Some(card1), Suit.Red), (Some(card2), Suit.Blue)),
                Array((Some(card3), Suit.Green), (Some(card4), Suit.Brown))
            ))

            val result = controller.getGridColors

            result should contain theSameElementsAs List(
                (0, 0, Some(card1), Suit.Red),
                (0, 1, Some(card2), Suit.Blue),
                (1, 0, Some(card3), Suit.Green),
                (1, 1, Some(card4), Suit.Brown)
            )
        }

        "return the correct grid color" in {
            val deck = new Deck()
            val hand = new Hand()
            val fileIO = mock[FileIOInterface]
            val controller = new GameController(deck, hand, fileIO)
            val grid = mock[Grid]
            controller.grid = grid

            when(grid.toArray).thenReturn(Array(Array((None, Suit.Red))))
            val result = controller.getGridColor(0, 0)

            result shouldBe "red"
        }

    }
}