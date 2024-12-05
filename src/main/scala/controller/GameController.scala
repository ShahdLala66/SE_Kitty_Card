// src/main/scala/controller/GameController.scala
package controller

import model.*
import model.cards.Card
import model.patterns.{GameModeFactory, PreSeenDeckStrategy, RandomStrategy, Strategy}
import util.*

class GameController extends Observer {
    private val deck = new Deck()
    private val grid = Grid.getInstance(3)
    private var observer: Option[Observer] = None
    private val commandManager = new CommandManager()
    private var currentState: GameState = new GameState(grid, List(), 0, 0) // Added points argument
    private var previousStates: List[Array[Array[String]]] = List()
    private var players: List[Player] = List()
    private var currentPlayerIndex: Int = 0
    private var previousHands: List[List[Card]] = List()

    def setObserver(observer: Observer): Unit = {
        this.observer = Some(observer)
    }

    def startGame(): Unit = {
        val mode = promptForGameMode()
        val game = new Game(deck, grid, null)
        val strategy = if (mode.toLowerCase == "multiplayer") promptForStrategy().getOrElse(new RandomStrategy()) else new RandomStrategy()
        val gameMode = GameModeFactory.createGameMode(mode, game, Some(strategy))
        game.gameMode = gameMode

        mode.toLowerCase match {
            case "singleplayer" =>
                observer.foreach(game.add)
                gameMode.playGame()

            case "multiplayer" =>
                val player1Name = promptForPlayerName("Player 1")
                val player2Name = promptForPlayerName("Player 2")
                observer.foreach(game.add)
                game.start(player1Name, player2Name)
                gameMode.playGame()

            case _ =>
                println("Invalid game mode selected.")
        }
    }

    def executeCommand(command: Command): Unit = {
        saveCurrentHandState()
        currentState = commandManager.executeCommand(command, currentState)
    }

    def undo(): Unit = {
        if (previousHands.nonEmpty) {
            players(currentPlayerIndex).hand = previousHands.head
            previousHands = previousHands.tail
        }
        commandManager.undo().foreach { state =>
            currentState = state
            observer.foreach(_.update(UndoEvent(currentState)))
        }
    }

    private def saveCurrentHandState(): Unit = {
        if (players.nonEmpty) {
            previousHands = players(currentPlayerIndex).hand :: previousHands
        }
    }

    def redo(): Unit = {
        commandManager.redo().foreach { state =>
            currentState = state
            observer.foreach(_.update(RedoEvent(currentState)))
        }
    }

    private def promptForPlayerName(player: String): String = {
        println(s"Enter the name for $player:")
        val name = scala.io.StdIn.readLine()
        if (name == null || name.trim.isEmpty) "Anonym" else name
    }

    def promptForGameMode(): String = {
        println("Enter the game mode (singleplayer/multiplayer):")
        scala.io.StdIn.readLine()
    }

    private def promptForSinglePlayerOption(): String = {
        println("Choose an option for Single Player mode:")
        println("1. Feed the kitties")
        println("2. Beat the kitty boss")
        print("Enter the number corresponding to your choice: ")

        scala.io.StdIn.readLine().trim match {
            case "1" => "Feed the kitties"
            case "2" => "Beat the kitty boss"
            case _ =>
                println("Invalid choice, defaulting to 'Feed the kitties'.")
                "Feed the kitties"
        }
    }

    private def promptForStrategy(): Option[Strategy] = {
        println("Choose a strategy for Multiplayer mode:")
        println("1. Random Strategy")
        println("2. Pre-Seen Deck Strategy")
        print("Enter the number corresponding to your choice: ")

        scala.io.StdIn.readLine().trim match {
            case "1" => Some(new RandomStrategy())
            case "2" => Some(new PreSeenDeckStrategy())
            case _ =>
                println("Invalid choice, defaulting to Random Strategy.")
                Some(new RandomStrategy())
        }
    }

    override def update(event: GameEvent): Unit = {
        observer.foreach(_.update(event))
    }

    def displayBadChoice(color: String): Unit = {
        println(s"Bad choice: $color")
    }

    def displayCatInColor(color: String): Unit = {
        println(s"Cat in color: $color")
    }

    def displayMeh(color: String): Unit = {
        println(s"Meh: $color")
    }
}