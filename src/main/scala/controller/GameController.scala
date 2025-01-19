package controller

import model.*
import model.FileIO.{FileIOInterface, FileIOXML}
import model.baseImp.*
import model.baseImp.Suit.Suit
import util.*
import util.command.{CommandManager, GameState, PlaceCardCommand}
import util.grid.GridFactory

import scala.util.{Failure, Success, Try}

class GameController(deck: Deck, hand: Hand, fileIOInterface: FileIOInterface) extends Observable with GameControllerInterface(deck: Deck, hand: Hand, fileIOInterface: FileIOInterface) {
    var gameMode: GameMode = _
    var grid: Grid = _
    var observers: List[Observer] = List()
    var playerIsAtTurn = true
    var currentPlayer: Player = _
    var player1: Player = _
    var player2: Player = _
    val commandManager = new CommandManager()
    var fileIO: FileIOInterface = new FileIOXML()
    var player1String: String = ""
    var player2String: String = ""
    private var counter = 0

    def getPlayers: List[Player] = List(player1, player2)

    def getCurrentState: GameState = currentState

    def getCurrentplayer: Player = currentPlayer

    def startGame(): Unit = {
        notifyObservers(AskForGameMode)
    }

    def setGameMode(mode: String): Unit = {
        mode.toLowerCase match {
            case "s" => gameMode = new SinglePlayerMode()
                notifyObservers(AskForLoadGame)

            case "m" => gameMode = new MultiPlayerMode()
                notifyObservers(AskForLoadGame)

            case _ =>
                println(s"Invalid game mode: $mode. Defaulting to Multi Player mode.")
                gameMode = new MultiPlayerMode()
        }
    }

    def startMultiPlayerGame(): Unit = {
        val (p1, p2) = addPlayers(player1String, player2String)
        player1 = p1
        player2 = p2
        currentPlayer = player1
        notifyObservers(UpdatePlayer(currentPlayer.name))
        distributeInitialCards()
        notifyObservers(UpdateGrid(grid))
        notifyObservers(ShowCardsForPlayer(currentPlayer.getHand))
        startGameLoop()
    }

    def askForInputAgain(): Unit = {
        playerIsAtTurn = true
    }

    def askForGameLoad(): Unit = {
        notifyObservers(AskForLoadGame)
    }

    def handleCommand(command: String): Unit = {
        command match {
            case "save" =>
                fileIO.save(this, grid)
                notifyObservers(GameSaved)
            case "load" =>
                Try(fileIO.load(this)) match {
                    case Success(_) =>
                    case Failure(e) => println(s"Error loading game: ${e.getMessage}")
                }
            case "start" =>
                grid = GridFactory.createGrid(3)
                currentState = new GameState(grid, List(player1, player2), 0, 0)
                notifyObservers(PromptForPlayerName)
            case "undo" => processCardPlacement("undo")
            case "redo" => processCardPlacement("redo")
            case "draw" => drawCardForCurrentPlayer()
        }
    }

    def loadGameState(state: GameState): Unit = {
        Try {
            println("Starting load process...")
            currentState = state
            gameMode.loadGame(this, state)
            println("Game loaded successfully")
        } match {
            case Success(_) =>
            case Failure(e) =>
                println(s"Error in loadGameState: ${e.getMessage}")
                e.printStackTrace()
                throw e
        }
    }

    private def distributeInitialCards(): Unit = {
        for (_ <- 1 to 3) {
            player1.drawCard(deck)
            player2.drawCard(deck)
        }
    }

    def drawCardForCurrentPlayer(): Unit = {
        currentPlayer.drawCard(deck) match {
            case Some(card) =>
                notifyObservers(CardDrawn(currentPlayer.name, card.toString))
                notifyObservers(ShowCardsForPlayer(currentPlayer.getHand))
                switchTurns()
            case None =>
                notifyObservers(InvalidPlacement)
        }
    }

    private def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean = {
        action() match {
            case Some(state) =>
                currentState = state
                notifyObservers(event)
                notifyObservers(ShowCardsForPlayer(currentPlayer.getHand))
                true
            case None =>
                false
        }
    }

    def handleCardPlacement(cardIndex: Int, x: Int, y: Int): Unit = {
        if (processCardPlacement(s"$cardIndex $x $y")) {
            switchTurns()
            playerIsAtTurn = true
        }
        if (!isGameOver) {
            playerIsAtTurn = true
        } else {
            displayFinalScores()
        }
    }

    private def processCardPlacement(input: String): Boolean = {
        input.trim.toLowerCase match {
            case "undo" =>
                executeUndoRedo(commandManager.undo, UndoEvent(currentState))
            case "redo" =>
                executeUndoRedo(commandManager.redo, RedoEvent(currentState))
            case _ =>
                Try {
                    val parts = input.split(" ")
                    val cardIndex = parts(0).toInt
                    val x = parts(1).toInt
                    val y = parts(2).toInt

                    currentPlayer.getHand.lift(cardIndex) match {
                        case Some(card: NumberCards) =>
                            if (grid.placeCard(x, y, card)) {
                                val pointsEarned = grid.calculatePoints(x, y)
                                val command = new PlaceCardCommand(grid, card, currentPlayer, pointsEarned, (x, y))
                                currentState = commandManager.executeCommand(command, currentState)
                                currentPlayer.addPoints(pointsEarned)
                                notifyObservers(CardPlacementSuccess(x, y, card.toString, pointsEarned, currentPlayer.name))

                                currentPlayer.removeCard(card)
                                true
                            } else {
                                notifyObservers(InvalidPlacement)
                                currentPlayer.removeCard(card)
                                switchTurns()

                                false
                            }
                        case _ =>
                            notifyObservers(InvalidPlacement)
                            false
                    }
                } match {
                    case Success(result) => result
                    case Failure(_) =>
                        notifyObservers(InvalidPlacement)
                        false
                }
        }
    }


    def startGameLoop(): Unit = {
        while (playerIsAtTurn) {
            Option(getCurrentplayer) match {
                case Some(player) =>
                    playerIsAtTurn = false
                    notifyObservers(PlayerTurn(getCurrentplayer.name))
                case None =>
            }
        }
    }

    def promptForPlayerName(player1String: String, player2String: String): Unit = {
        if (counter == 0) {
            this.player1String = player1String
            this.player2String = player2String
            counter += 1
            notifyObservers(UpdatePlayers(Player(player1String), Player(player2String)))
        } else {
            println("Game already started")
        }
        gameMode.startGame(this)
    }

    def switchTurns(): Unit = {
        currentPlayer = if (currentPlayer == player1) player2 else player1
        notifyObservers(UpdatePlayer(currentPlayer.name))
        notifyObservers(UpdateGrid(grid))
        notifyObservers(ShowCardsForPlayer(currentPlayer.getHand))
    }

    private def displayFinalScores(): Unit = {
        notifyObservers(GameOver(player1.name, player1.points, player2.name, player2.points))

    }

    private def addPlayers(player1Name: String, player2Name: String): (Player, Player) = {
        val player1 = Player(player1Name)
        val player2 = Player(player2Name)
        (player1, player2)
    }

    def getGridColors: List[(Int, Int, Option[CardInterface], Suit)] = {
        grid.toArray.zipWithIndex.flatMap { case (row, x) =>
            row.zipWithIndex.map { case ((card, color), y) =>
                (x, y, card, color)
            }
        }.toList
    }

    def getGridColorsFromGrid(grid: Grid): List[(Int, Int, Option[CardInterface], Suit)] = {
        grid.toArray.zipWithIndex.flatMap { case (row, x) =>
            row.zipWithIndex.map { case ((card, color), y) =>
                (x, y, card, color)
            }
        }.toList
    }
    
    def getPlayer1: String = player1.name
    
    def getPlayer2: String = player2.name

    def getCurrentPlayerString: String = currentPlayer.name

    def isGameOver: Boolean = {
        deck.size <= 0 || grid.isFull
    }

    def getGridColor(x: Int, y: Int): String = {
        grid.toArray(x)(y)._2.toString.toLowerCase
    }

}