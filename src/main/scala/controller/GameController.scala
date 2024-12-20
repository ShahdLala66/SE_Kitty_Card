// src/main/scala/controller/GameController.scala
package controller

import model.*
import model.cards.Suit.Suit
import model.cards.{Card, NumberCards}
import util.*
import util.grid.GridFactory

class GameController extends Observable {
    private val deck = new Deck()
    private val grid = GridFactory.createGrid(3) // Use GridFactory to create a grid with random colors
    private var observer: Option[Observer] = None
    private val game = new Game(deck, grid, this)
    private var gameEndCallback: () => Unit = () => {}


    def startGame(): Unit = {
        val game = new Game(deck, grid, this)
        //game.askForPlayerNames()
        game.start(player1, player2)
    }

    var player1: String = ""
    var player2: String = ""
    private var counter = 0

    def promptForPlayerName(player1: String, player2: String): Any = {
        if (counter == 0) {
            this.player1 = player1
            this.player2 = player2
            counter += 1
            game.addPlayers(player1, player2)
        } else {
            print("Game already started")
        }
    }

    def getGridColors: List[(Int, Int, Option[NumberCards], Suit)] = {
        grid.toArray.zipWithIndex.flatMap { case (row, x) =>
            row.zipWithIndex.map { case ((card, color), y) =>
                (x, y, card, color)
            }
        }.toList
    }

    def setInput(input: String): Unit = {
        game.setInput(input)
    }

    def getCurrentplayer: Player = game.getCurrentplayer

    def askForPlayerNames(): Unit = {
        notifyObservers(PromptForPlayerName)
    }

    def updatePlayers(player1: Player, player2: Player): Unit =
        notifyObservers(UpdatePlayers(player1, player2))

    def updateCurrentPlayer(player: Player): Unit =
        notifyObservers(UpdatePlayer(player))

    def showCardsForPlayer(hand: List[Card]): Unit =
        notifyObservers(ShowCardsForPlayer(hand))

    def notifyPlayerTurn(playerName: String): Unit = notifyObservers(PlayerTurn(playerName))

    def waitForPlayerInput(): Unit =
        notifyObservers(WaitForPlayerInput)

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

    def gameOver(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int): Unit =
        notifyObservers(GameOver(player1Name, player1Points, player2Name, player2Points))
        
    def intro(): Unit = {
        notifyObservers(GameStart)
    }
}