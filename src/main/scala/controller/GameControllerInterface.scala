// src/main/scala/controller/GameControllerInterface.scala
package controller

import model.cardComp.NumberCards
import model.cardComp.Suit.Suit
import model.playerComp.Player
import util.Observer

trait GameControllerInterface {
    def setObserver(observer: Observer): Unit
    def startGame(): Unit
    def promptForPlayerName(player1: String, player2: String): Unit
    def getGridColors: List[(Int, Int, Option[NumberCards], Suit)]
    def getCurrentplayer: Player
}