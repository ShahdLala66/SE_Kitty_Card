package controller

import model.*
import model.baseImp.{Deck, Hand}
import util.*

import java.net.URI
import java.awt.Desktop
import scala.util.{Failure, Success, Try}

// Trait for Game Modes
trait GameMode {
  def startGame(controller: GameController): Unit
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
}

// Multi Player Mode
class MultiPlayerMode extends GameMode {
  override def startGame(controller: GameController): Unit = {
    controller.startMultiPlayerGame()
  }
}