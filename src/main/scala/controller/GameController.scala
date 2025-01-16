package controller

import model.*
import model.baseImp.{Deck, Grid, Hand, NumberCards, Player}
import model.baseImp.Suit.Suit
import util.*
import util.command.{CommandManager, GameState, PlaceCardCommand}
import util.grid.GridFactory

import scala.util.{Failure, Success, Try}

class GameController(deck: Deck, hand: Hand) extends Observable with GameControllerInterface(deck: Deck, hand: Hand) {
    private var gameMode: GameMode = _ // No default mode initially
    private val grid = GridFactory.createGrid(3)
    private var observers: List[Observer] = List()
    private var playerIsAtTurn = true
    private var currentPlayer: Player = _
    private var player1: Player = _
    private var player2: Player = _
    private val commandManager = new CommandManager()
    private var currentState: GameState = new GameState(grid, List(), 0, 0)

    var player1String: String = ""
    var player2String: String = ""
    private var counter = 0

    // Start the game and ask for game mode selection
    def startGame(): Unit = {
       notifyObservers(AskForGameMode) // Notify observers to ask for the game mode
    }

    // This method will be triggered when the observer event is received with the chosen game mode
     def setGameMode(mode: String): Unit = {
        mode.toLowerCase match {
            case "s" => gameMode = new SinglePlayerMode()
            case "m"  => gameMode = new MultiPlayerMode()
            case _ =>
                println(s"Invalid game mode: $mode. Defaulting to Multi Player mode.")
                gameMode = new MultiPlayerMode() // Fall back to multiplayer if invalid mode
        }

        // Once the game mode is set, start the game in the selected mode
        gameMode.startGame(this)
    }

     def startMultiPlayerGame(): Unit = {
        notifyObservers(PromptForPlayerName)
        val (p1, p2) = addPlayers(player1String, player2String)
        player1 = p1
        player2 = p2
        currentPlayer = player1
        updateCurrentPlayer(currentPlayer)
        distributeInitialCards()
        updateGrid(grid)
        showCardsForPlayer(currentPlayer.getHand)
        startGameLoop()
    }

     def askForInputAgain(): Unit = {
         playerIsAtTurn = true
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
                cardDrawn(currentPlayer.name, card.toString)
                showCardsForPlayer(currentPlayer.getHand)
                switchTurns()
            case None =>
                invalidPlacement()
        }
    }

    private def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean = {
        action() match {
            case Some(state) =>
                currentState = state
                notifyUndoRedo(event)
                showCardsForPlayer(currentPlayer.getHand)
                true
            case None =>
                false
        }
    }

    def handleCardPlacement(cardIndex: Int, x: Int, y: Int): Unit = {
        if (processCardPlacement(s"$cardIndex $x $y")) {
            switchTurns()
            playerIsAtTurn = true // Resume loop for next turn
        }
        if (!isGameOver) {
            playerIsAtTurn = true // Reset flag if game isn't over
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
                                cardPlacementSuccess(x, y, card.toString, pointsEarned)
                                currentPlayer.removeCard(card)
                                true
                            } else {
                                invalidPlacement()
                                false
                            }
                        case _ =>
                            invalidPlacement()
                            false
                    }
                } match {
                    case Success(result) => result
                    case Failure(_) =>
                        invalidPlacement()
                        false
                }
        }
    }

    def handleCommand(command: String): Unit = {
        command match {
            case "undo" => processCardPlacement("undo")
            case "redo" => processCardPlacement("redo")
            case "draw" => drawCardForCurrentPlayer()
        }
    }

    def startGameLoop(): Unit = {
        while (playerIsAtTurn) {
            if (getCurrentplayer != null) {
                playerIsAtTurn = false // Stop loop after getting current player
                notifyPlayerTurn(getCurrentplayer.name)
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
    }

    def getCurrentplayer: Player = currentPlayer

    def switchTurns(): Unit = {
        currentPlayer = if (currentPlayer == player1) player2 else player1
        updateCurrentPlayer(currentPlayer)
        updateGrid(grid)
        showCardsForPlayer(currentPlayer.getHand)
    }

    private def displayFinalScores(): Unit = {
        gameOver(player1.name, player1.points, player2.name, player2.points)
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

    def isGameOver: Boolean = {
        deck.size <= 0 || grid.isFull
    }

    def getGridColor(x: Int, y: Int): String = {
        grid.toArray(x)(y)._2.toString.toLowerCase
    }

    // Observer notification methods
    def updatePlayers(player1String: Player, player2String: Player): Unit =
        notifyObservers(UpdatePlayers(player1String, player2String))

    def updateCurrentPlayer(player: Player): Unit =
        notifyObservers(UpdatePlayer(player.name))

    def showCardsForPlayer(hand: List[CardInterface]): Unit =
        notifyObservers(ShowCardsForPlayer(hand))

    def notifyPlayerTurn(playerName: String): Unit =
        notifyObservers(PlayerTurn(playerName))

    def cardDrawn(playerName: String, card: String): Unit =
        notifyObservers(CardDrawn(playerName, card))

    def invalidPlacement(): Unit =
        notifyObservers(InvalidPlacement)

    def notifyUndoRedo(event: GameEvent): Unit =
        notifyObservers(event)

    def cardPlacementSuccess(x: Int, y: Int, card: String, points: Int): Unit =
        notifyObservers(CardPlacementSuccess(x, y, card, points))

    def updateGrid(grid: Grid): Unit =
        notifyObservers(UpdateGrid(grid))

    def gameOver(player1StringName: String, player1StringPoints: Int, player2StringName: String, player2StringPoints: Int): Unit =
        notifyObservers(GameOver(player1StringName, player1StringPoints, player2StringName, player2StringPoints))
}