// src/main/scala/controller/GameController.scala
package controller

import model.*
import model.logik.Game
import model.objects.{Deck, Grid}
import model.patterns.*
import util.*
import util.command.CommandManager
import util.observer.*

import scala.io.StdIn.readLine

class GameController extends Observer {
    private val deck = new Deck()
    private val grid = Grid.getInstance(3)
    private var observer: Option[Observer] = None
    private val commandManager = new CommandManager()
    private var selectedStrategy: Option[Strategy] = None


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
            case "singleplayer" | "s" =>
                observer.foreach(game.add)
                gameMode.playGame()

            case "multiplayer" | "m" =>
                val player1Name = promptForPlayerName("Player 1")
                val player2Name = promptForPlayerName("Player 2")
                observer.foreach(game.add)
                game.start(player1Name, player2Name)
                gameMode.playGame()

            case _ =>
                observer.foreach(_.update(Invalid()))
        }
    }

    private def promptForPlayerName(player: String): String = {
        observer.foreach(_.update(PromptForPlayerName(player)))
        val name = scala.io.StdIn.readLine()
        if (name == null || name.trim.isEmpty) "Anonym" else name
    }

    def promptForGameMode(): String = {
        observer.foreach(_.update(PromptForGameMode()))
        scala.io.StdIn.readLine()
    }

    private def promptForSinglePlayerOption(): String = {
        observer.foreach(_.update(SelectSinglePlayerOption()))
        "Singleplayer"
    }

    private def promptForStrategy(): Option[Strategy] = {
        observer.foreach(_.update(StrategySelection()))
        val strategy = readLine().trim match {
            case "1" => new RandomStrategy()
            case "2" => new PreSeenDeckStrategy()
            case _ =>
                observer.foreach(_.update(Invalid()))
                new RandomStrategy()
        }
        selectedStrategy
    }

    def setSelectedStrategy(strategy: Strategy): Unit = {
        selectedStrategy = Some(strategy)
    }

    override def update(event: GameEvent): Unit = {
        observer.foreach(_.update(event))
    }
}