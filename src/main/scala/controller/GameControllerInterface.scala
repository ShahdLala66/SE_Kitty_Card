// src/main/scala/controller/GameControllerInterface.scala
package controller

import model.fileIOComp.FileIOInterface
import model.gameModelComp.baseImp.Suit.Suit
import model.gameModelComp.baseImp.{Deck, Hand, Player}
import model.gameModelComp.{CardInterface, PlayerInterface}
import util.{Observable, Observer}
import util.command.GameState

import scala.compiletime.uninitialized

trait GameControllerInterface(deck: Deck, hand: Hand, fileIOInterface: FileIOInterface) extends Observable {

    var currentState: GameState = uninitialized

    def setGameMode(mode: String): Unit

    def startMultiPlayerGame(): Unit

    //def add(observer: Observer): Unit

    def startGame(): Unit

    def startGameLoop(): Unit

    def handleCommand(command: String): Unit

    def handleCardPlacement(cardIndex: Int, x: Int, y: Int): Unit

    def promptForPlayerName(player1: String, player2: String): Unit

    def getGridColors: List[(Int, Int, Option[CardInterface], Suit)]

    def askForGameLoad(): Unit

    def isGameOver: Boolean

    def getCurrentplayer: PlayerInterface

    def getCurrentPlayerString: String

    def getGridColor(x: Int, y: Int): String

    def getCurrentState: GameState //savw

    def loadGameState(gameState: GameState): Unit //load

    def getPlayers: List[Player] //save

    def askForInputAgain(): Unit

    def getPlayer1: String

    def getPlayer2: String

    def getCurrentPlayerPoints: Int
}

