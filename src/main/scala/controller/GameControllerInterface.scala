// src/main/scala/controller/GameControllerInterface.scala
package controller

import model.cardComp.baseImp.NumberCards
import model.cardComp.baseImp.Suit.Suit
import model.playerComp.baseImp.Player
import util.Observer

trait GameControllerInterface {
    def setObserver(observer: Observer): Unit
    def startGame(): Unit
    def promptForPlayerName(player1: String, player2: String): Unit
    def getGridColors: List[(Int, Int, Option[NumberCards], Suit)]
    def getCurrentplayer: Player
}