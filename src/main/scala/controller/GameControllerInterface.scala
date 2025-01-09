// src/main/scala/controller/GameControllerInterface.scala
package controller

import model.cardComp.CardInterface
import model.cardComp.baseImp.Grid
import model.cardComp.baseImp.Suit.Suit
import model.playerComp.PlayerInterface
import model.playerComp.baseImp.Player
import util.{GameEvent, Observer}

trait GameControllerInterface {
    def add(observer: Observer): Unit
    
    def startGame(): Unit

    def startGameLoop(): Unit

    def handleCommand(command: String): Unit

    def handleCardPlacement(cardIndex: Int, x: Int, y: Int): Unit

    def promptForPlayerName(player1: String, player2: String): Unit

    def getGridColors: List[(Int, Int, Option[CardInterface], Suit)]

    def isGameOver: Boolean

    def getCurrentplayer: PlayerInterface

    def getGridColor(x: Int, y: Int): String
    
    def updatePlayers(player1: Player, player2: Player): Unit

    def updateCurrentPlayer(player: Player): Unit

    def showCardsForPlayer(hand: List[CardInterface]): Unit

    def notifyPlayerTurn(playerName: String): Unit

    def cardDrawn(playerName: String, card: String): Unit

    def invalidPlacement(): Unit

    def cardPlacementSuccess(x: Int, y: Int, card: String, points: Int): Unit

    def notifyUndoRedo(event: GameEvent): Unit

    def updateGrid(grid: Grid): Unit

    def gameOver(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int): Unit
}