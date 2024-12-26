// src/main/scala/controller/GameControllerInterface.scala
package controller

import model.cardComp.CardInterface
import model.cardComp.baseImp.Suit.Suit
import model.playerComp.PlayerInterface
import util.Observer

trait GameControllerInterface {
    def addObserver(observer: Observer): Unit
    def startGame(): Unit
    def startGameLoop(): Unit
    def handleCommand(command: String): Unit
    def handleCardPlacement(cardIndex: Int, x: Int, y: Int): Unit
    def promptForPlayerName(player1: String, player2: String): Unit
    def getGridColors: List[(Int, Int, Option[CardInterface], Suit)]
    def isGameOver: Boolean
    def getCurrentplayer: PlayerInterface
    def getGridColor(x: Int, y: Int): String
}