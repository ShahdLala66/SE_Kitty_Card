package controller

import model.*
import util.*
import util.command.GameState
import util.grid.GridFactory

import java.awt.Desktop
import java.net.URI
import scala.util.{Failure, Success, Try}

// Trait for Game Modes
trait GameMode {
  def startGame(controller: GameController): Unit

  def loadGame(controller: GameController, savedState: GameState): Unit

}

// Single Player Mode
class SinglePlayerMode extends GameMode {
  override def startGame(controller: GameController): Unit = {
    Try(Desktop.getDesktop.browse(new URI("https://youtu.be/dQw4w9WgXcQ"))) match {
      case Success(_) => println("Single player mode triggered. Joke on you no such thing as Single Game hahaha")
        controller.startMultiPlayerGame()

      case Failure(e) => println(s"Failed to open URL: ${e.getMessage}")
    }
  }

  def loadGame(controller: GameController, savedState: GameState): Unit = {
    println("Single player mode triggered. Joke on you no such thing as Single Game hahaha")
  }
}

// Multi Player Mode
class MultiPlayerMode extends GameMode {
  override def startGame(controller: GameController): Unit = {
    controller.grid = GridFactory.createGrid(3)
    //controller.notifyObservers(PromptForPlayerName)
   // controller.startGameLoop()
    controller.startMultiPlayerGame()


  }

  override def loadGame(controller: GameController, savedState: GameState): Unit = {
    // Initialize GUI first
    controller.notifyObservers(InitializeGUIForLoad)

    // Set up saved game state
    controller.grid = savedState.getGrid
    val players = savedState.getPlayers
    if (players.nonEmpty) {
      controller.player1 = players.head
      controller.player2 = players(1)
      controller.currentPlayer = savedState.getCurrentPlayer
    }

    // Update GUI with saved state
    controller.notifyObservers(UpdateLoadedGame(
      controller.getGridColorsFromGrid(controller.grid),
      controller.currentPlayer,
      controller.player1,
      controller.player2,
      controller.currentPlayer.getHand
    ))

    // Start game loop
    controller.startGameLoop()
  }

}